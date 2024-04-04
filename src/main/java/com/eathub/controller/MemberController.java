package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.*;
import com.eathub.entity.ENUM.MEMBER_TYPE;
import com.eathub.entity.Members;
import com.eathub.entity.RestaurantInfo;
import com.eathub.service.MemberService;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @ModelAttribute("page")
    public String page() {
        return "members";
    }

    @GetMapping("/my")
    public String myPage(MemberJoinDTO memberJoinDTO, Model model, HttpSession session) {
        model.addAttribute("memberJoinDTO", memberJoinDTO);
        List<MyPageDTO> zzimRestaurantList = restaurantService.getZzimRestaurantList((Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ));
        model.addAttribute("myPageDTO", zzimRestaurantList);
        return "/members/myPage";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        model.addAttribute("loginDTO", new LoginDTO());
        log.info("login 페이지 이동");
        return "/members/loginForm";
    }

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

    @GetMapping("/update")
    public String updatePage(Model model, HttpSession session) {
        Members member = memberService.selectMemberById((String) session.getAttribute(SessionConf.LOGIN_MEMBER));
        model.addAttribute("memberUpdateDTO", member);
        return "/members/updateForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute @Validated MemberUpdateDTO memberUpdateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/members/updateForm";
        }
        memberService.updateMember(
                Members.builder()
                        .member_id(memberUpdateDTO.getMember_id())
                        .member_pwd(memberUpdateDTO.getMember_pwd())
                        .member_email(memberUpdateDTO.getMember_email())
                        .member_phone(memberUpdateDTO.getMember_phone())
                        .build()
        );
        return "redirect:/members/my";
    }

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

    @PostMapping("/restaurant/join")
    public String joinRestaurant(@ModelAttribute RestaurantJoinDTO restaurantJoinDTO, HttpSession session) throws ParseException {

        log.info("restaurant ={}", restaurantJoinDTO.toString());

        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);

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

}
