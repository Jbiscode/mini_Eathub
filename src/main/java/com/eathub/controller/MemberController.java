package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.*;
import com.eathub.entity.ENUM.MEMBER_TYPE;
import com.eathub.entity.ENUM.MENU_TYPE;
import com.eathub.entity.Members;
import com.eathub.entity.RestaurantInfo;
import com.eathub.service.MemberService;
import com.eathub.service.NCPObjectStorageService;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final RestaurantService restaurantService;
    private final NCPObjectStorageService ncpObjectStorageService;

    @ModelAttribute("page")
    public String page() {
        return "members";
    }

    /**
        * 사용자의 마이페이지를 반환하는 메소드입니다.
        * 
        * @param memberJoinDTO MemberJoinDTO 객체
        * @param model Model 객체
        * @param session HttpSession 객체
        * @return 사용자의 마이페이지 경로
        */
    @GetMapping("/my")
    public String myPage(MemberJoinDTO memberJoinDTO, Model model, HttpSession session) {
        MEMBER_TYPE mem_type = (MEMBER_TYPE) session.getAttribute(SessionConf.LOGIN_MEMBER_TYPE);
        Long mem_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);

        if(mem_type.equals(MEMBER_TYPE.OWNER)){
            List<MyPageDTO> ownerRestaurantList = restaurantService.getOwnerRestaurantList(mem_seq);
            model.addAttribute("myPageDTO", ownerRestaurantList);
            model.addAttribute("restaurantJoinDTO", new RestaurantJoinDTO());
            return "/members/ownerMyPage";
        }

        // 로그인 회원 이름받아오기
        String mem_id = (String) session.getAttribute(SessionConf.LOGIN_MEMBER);
        Members members = memberService.selectMemberById(mem_id);
        memberJoinDTO.setMember_name(members.getMember_name());

        // 추천 레스토랑 리스트 불러오기 (랜덤 / 찜 아닌 것)
        List<SearchResultDTO> recommendRestaurantList = restaurantService.getRandomRestaurant(mem_seq);

        model.addAttribute("recommendRestaurantList", recommendRestaurantList);
        model.addAttribute("memberJoinDTO", memberJoinDTO);
        List<MyPageDTO> zzimRestaurantList = restaurantService.getZzimRestaurantList(mem_seq);
        model.addAttribute("myPageDTO", zzimRestaurantList);
        return "/members/myPage";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        model.addAttribute("loginDTO", new LoginDTO());
        log.info("login 페이지 이동");
        return "/members/loginForm";
    }

    /**
     * 로그인을 처리하는 메소드입니다.
     * 
     * @param loginDTO 로그인 정보를 담고 있는 LoginDTO 객체
     * @param bindingResult 데이터 바인딩 결과를 담고 있는 BindingResult 객체
     * @param redirectURL 리다이렉트할 URL을 나타내는 문자열
     * @param request HttpServletRequest 객체
     * @return 로그인 성공 시 리다이렉트할 URL
     */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        log.info("로그인 시도");
        Members loginMember = memberService.login(loginDTO, bindingResult);

        if (loginMember == null) {
            log.error("로그인 실패");
            return "/members/loginForm";
        }

        HttpSession session = request.getSession();

        // 세션에 로그인 정보 저장
        session.setAttribute(SessionConf.LOGIN_MEMBER_SEQ, loginMember.getMember_seq());
        session.setAttribute(SessionConf.LOGIN_MEMBER_TYPE,loginMember.getMember_type());
        session.setAttribute(SessionConf.LOGIN_MEMBER, loginMember.getMember_id());
        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 회원가입 종류 선택
     *
     * @return
     */
    @GetMapping("/join/select")
    public String login() {
        return "/members/joinSelect";
    }


    /**
     * 고객 회원가입 폼
     *
     * @param model
     * @return
     */
    @GetMapping("/join/customer")
    public String joinCustomer(Model model) {
        model.addAttribute("memberJoinDTO", new MemberJoinDTO(MEMBER_TYPE.CUSTOMER));
        return "/members/joinForm";
    }

    @PostMapping("/join/customer")
    public String joinCustomer(@ModelAttribute @Validated MemberJoinDTO memberJoinDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/members/joinForm";
        }
        memberService.insertMember(
                Members.builder()
                        .member_id(memberJoinDTO.getMember_id())
                        .member_pwd(memberJoinDTO.getMember_pwd())
                        .member_name(memberJoinDTO.getMember_name())
                        .member_email(memberJoinDTO.getMember_email())
                        .member_phone(memberJoinDTO.getMember_phone())
                        .member_type(MEMBER_TYPE.CUSTOMER)
                        .build()
        );

        model.addAttribute("loginDTO", new LoginDTO(memberJoinDTO.getMember_id(), ""));
        return "redirect:/members/login";
    }


    /**
     * 사장님 회원가입 폼
     *
     * @param model
     * @return
     */
    @GetMapping("/join/owner")
    public String joinOwner(Model model) {
        model.addAttribute("memberJoinDTO", new MemberJoinDTO(MEMBER_TYPE.OWNER));
        return "/members/joinForm";
    }

    /**
        * 주어진 MemberJoinDTO를 사용하여 소유자 회원가입을 처리하고, 결과에 따라 적절한 뷰를 반환합니다.
        * 
        * @param memberJoinDTO 회원가입 정보를 담고 있는 MemberJoinDTO 객체
        * @param bindingResult 유효성 검사 결과를 담고 있는 BindingResult 객체
        * @param model 뷰에 전달할 데이터를 담고 있는 Model 객체
        * @return 회원가입이 성공한 경우 로그인 페이지로 리다이렉트하고, 실패한 경우 회원가입 폼을 보여주는 뷰
        */
    @PostMapping("/join/owner")
    public String joinOwner(@Validated MemberJoinDTO memberJoinDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/members/joinForm";
        }
        memberService.insertMember(
                Members.builder()
                        .member_id(memberJoinDTO.getMember_id())
                        .member_pwd(memberJoinDTO.getMember_pwd())
                        .member_name(memberJoinDTO.getMember_name())
                        .member_email(memberJoinDTO.getMember_email())
                        .member_phone(memberJoinDTO.getMember_phone())
                        .member_type(MEMBER_TYPE.OWNER)
                        .build()
        );

        model.addAttribute("loginDTO", new LoginDTO(memberJoinDTO.getMember_id(), ""));
        return "redirect:/members/login";
    }

    /**
        * updatePage 메소드는 회원 정보 수정 페이지로 이동하는 기능을 수행합니다.
        * 
        * @param model    Model 객체
        * @param session  HttpSession 객체
        * @return         회원 정보 수정 페이지 경로
        */
    @GetMapping("/update")
    public String updatePage(Model model, HttpSession session) {
        Members member = memberService.selectMemberById((String) session.getAttribute(SessionConf.LOGIN_MEMBER));
        model.addAttribute("memberUpdateDTO", member);
        return "/members/updateForm";
    }

    /**
        * 회원 정보를 업데이트하는 메소드입니다.
        *
        * @param memberUpdateDTO 업데이트할 회원 정보를 담고 있는 DTO 객체
        * @param bindingResult   데이터 유효성 검사 결과를 담고 있는 BindingResult 객체
        * @return 업데이트가 성공하면 "/members/my"로 리다이렉트하고, 유효성 검사에 실패하면 "/members/updateForm"으로 이동합니다.
        */
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated MemberUpdateDTO memberUpdateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("업데이트실패");
            return "/members/updateForm";
        }
        memberService.update(
                Members.builder()
                        .member_id(memberUpdateDTO.getMember_id())
                        .member_name(memberUpdateDTO.getMember_name())
                        .member_pwd(memberUpdateDTO.getMember_pwd())
                        .member_email(memberUpdateDTO.getMember_email())
                        .member_phone(memberUpdateDTO.getMember_phone())
                        .build()
        );
        return "redirect:/members/my";
    }

    /**
        * 가게 등록 폼으로 이동하는 메소드입니다.
        * 멤버 타입이 OWNER인 경우에만 등록 폼으로 이동합니다.
        * 멤버 타입이 CUSTOMER인 경우에는 홈 화면으로 리다이렉트합니다.
        * 지역 리스트와 카테고리 리스트를 받아와 모델에 추가합니다.
        * 
        * @param model    모델 객체
        * @param session  HttpSession 객체
        * @return         가게 등록 폼 페이지 경로
        */
    @GetMapping("/restaurant/join")
    public String joinRestaurant(Model model, HttpSession session){

        // 멤버 타입 확인 후, OWNER인 경우만 등록폼을 이동
        MEMBER_TYPE memberType =(MEMBER_TYPE) session.getAttribute(SessionConf.LOGIN_MEMBER_TYPE);
        if (memberType.equals(MEMBER_TYPE.CUSTOMER)){
            return "redirect:/";
        }

        // 지역리스트, 카테고리 리스트 받아오기
        Map<String, String> locationList = restaurantService.getLocationList();
        List<CategoryDTO> categoryList = restaurantService.getCategoryList();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("locationList",locationList);
        model.addAttribute("restaurantJoinDTO", new RestaurantJoinDTO());
        return "/members/restaurantJoinForm";
    }

    /**
        * 가게 등록을 처리하는 메소드입니다.
        * 
        * @param restaurantJoinDTO 가게 등록 정보를 담고 있는 DTO 객체
        * @param session HttpSession 객체
        * @return 회원 페이지로 리다이렉트하는 문자열
        * @throws ParseException 날짜 형식 변환 중 발생하는 예외
        */
    @PostMapping("/restaurant/join")
    public String joinRestaurant(@Validated @ModelAttribute RestaurantJoinDTO restaurantJoinDTO, BindingResult bindingResult,Model model,HttpSession session) throws ParseException {

        log.info("restaurant ={}", restaurantJoinDTO.toString());

        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        Map<String, String> locationList = restaurantService.getLocationList();
        List<CategoryDTO> categoryList = restaurantService.getCategoryList();

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("locationList",locationList);
            return "/members/restaurantJoinForm";
        }

        // Time 형으로 형변환
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String openHour = restaurantJoinDTO.getOpenHour();
        String closeHour = restaurantJoinDTO.getCloseHour();
        Time parsedOpenHour = new Time(format.parse(openHour).getTime());
        Time parsedCloseHour = new Time(format.parse(closeHour).getTime());

        // 가게 등록
        restaurantService.insertRestaurant(
                RestaurantInfo.builder()
                        .member_seq(member_seq)
                        .category_seq(restaurantJoinDTO.getCategory_seq())
                        .restaurant_name(restaurantJoinDTO.getRestaurant_name())
                        .tag(restaurantJoinDTO.getTag())
                        .location(restaurantJoinDTO.getLocation())
                        .description(restaurantJoinDTO.getDescription())
                        .phone(restaurantJoinDTO.getPhone())
                        .zipcode(restaurantJoinDTO.getZipcode())
                        .address1(restaurantJoinDTO.getAddress1())
                        .address2(restaurantJoinDTO.getAddress2())
                        .openHour(parsedOpenHour)
                        .closeHour(parsedCloseHour)
                        .closedDay(restaurantJoinDTO.getClosedDay())
                        .build()
        );

        // 관리자 승인을 위한 승인대기 리스트에 등록
        RestaurantInfo savedRestaurant =  restaurantService.selectSavedRestaurant(restaurantJoinDTO);
        Long savedRestaurant_seq =  savedRestaurant.getRestaurant_seq();

        restaurantService.insertRestaurantStatus(
                RestaurantInfo.builder()
                        .restaurant_seq(savedRestaurant_seq)
                        .build()
        );

        return "redirect:/members/my";
    }
    @GetMapping("/restaurant/{restaurantSeq}/menu/add")
    public String showForm(@PathVariable("restaurantSeq") Long restaurant_seq , Model model,HttpSession session){
        Long OwnerSeq = restaurantService.selectRestaurantInfo(restaurant_seq).getMember_seq();
        if (!OwnerSeq.equals(session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ))){
            log.error("접근권한이 없습니다.오너{} 유저{}", OwnerSeq, session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ));
            return "redirect:/";
        }

        MenuFormDTOWrapper menuForm = new MenuFormDTOWrapper();
        menuForm.getMenuList().add(new MenuFormDTO()); // 기본 메뉴 폼 추가

        model.addAttribute("menuForm", menuForm);
        model.addAttribute("menuTypeOptions", MENU_TYPE.values());
        return "/restaurant/addMenus";
    }

    @PostMapping("/restaurant/{restaurantSeq}/menu/add")
    public String saveForm(@ModelAttribute MenuFormDTOWrapper menuFormDTOWrapper,
                            @PathVariable("restaurantSeq") Long restaurant_seq,
                            HttpSession session){
        String BucketFolderName = "storage/";
        String UUID;
        String imageOriginalName;
        File file;

        String filepath = session.getServletContext().getRealPath("WEB-INF/storage");
        System.out.println("실제폴더 = " + filepath);

        List<MenuFormDTO> menuList = menuFormDTOWrapper.getMenuList();

        for (MenuFormDTO menu : menuList) {
            log.info("menu = {}", menu);
            // 이미지 파일 저장
            if (menu.getMenu_image() != null) {
                imageOriginalName = menu.getMenu_image().getOriginalFilename();
                // NCP Object Storage에 이미지 업로드
                UUID = ncpObjectStorageService.uploadFile(SessionConf.BUCKET_NAME, BucketFolderName, menu.getMenu_image());
                file = new File(filepath, imageOriginalName);
                try {
                    menu.getMenu_image().transferTo(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                menu.setMenu_image_name(imageOriginalName);
                menu.setMenu_image_path(UUID);
            }
        }
        // DB에 메뉴 저장
        restaurantService.insertRestaurantMenu(restaurant_seq, menuFormDTOWrapper);

        return "redirect:/members/my";
    }

    @GetMapping("/restaurant/{restaurantSeq}/edit")
    public String editForm(@PathVariable("restaurantSeq") Long restaurant_seq, Model model){

        model.addAttribute("restaurantDetailDTO", new RestaurantDetailDTO());


        return "restaurant/restaurantDetailForm";
    }

    @PostMapping("/restaurant/{restaurantSeq}/edit")
    public String detailSaveForm(@PathVariable("restaurantSeq") Long restaurant_seq, @ModelAttribute RestaurantDetailDTO restaurantDetailDTO, BindingResult bindingResult,HttpSession session){

        String BucketFolderName = "storage/";
        String UUID;
        String imageOriginalName;
        File file;

        restaurantDetailDTO.setRestaurant_seq(restaurant_seq);
        log.info("restaurantDetailInfo={}", restaurantDetailDTO);

        if(bindingResult.hasErrors()){
            log.info("bindingResult={}",bindingResult);
        }



        if (restaurantDetailDTO.getRestaurant_image() != null) {
            imageOriginalName = restaurantDetailDTO.getRestaurant_image().getOriginalFilename();

            UUID = ncpObjectStorageService.uploadFile(SessionConf.BUCKET_NAME, BucketFolderName, restaurantDetailDTO.getRestaurant_image());
            restaurantDetailDTO.setImage_url(UUID);
            restaurantService.saveRestaurantDetail(restaurantDetailDTO);
            restaurantService.saveRestaurantImage(UUID,restaurant_seq);
        }else{
            restaurantService.saveRestaurantDetail(restaurantDetailDTO);
        }


        return "redirect:/members/my";
    }
}
