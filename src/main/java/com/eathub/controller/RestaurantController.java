package com.eathub.controller;


import com.eathub.conf.SessionConf;
import com.eathub.dto.*;
import com.eathub.entity.Reservation;
import com.eathub.entity.RestaurantDetail;
import com.eathub.entity.RestaurantInfo;
import com.eathub.service.RestaurantService;
import com.eathub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    /**
        * 해당 레스토랑의 정보를 가져와서 모델에 추가하고, 레스토랑 상세 정보 페이지로 이동합니다.
        * 
        * @param restaurant_seq 레스토랑 시퀀스
        * @param model 모델 객체
        * @param session 세션 객체
        * @return 레스토랑 상세 정보 페이지 경로
        */
    @GetMapping("/detail/{restaurant_seq}")
    public String restaurantInfo(@PathVariable Long restaurant_seq,Model model,HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        RestaurantDetailDTO restaurantDetailDTO = restaurantService.getRestaurantDetail(restaurant_seq);
        if(restaurantDetailDTO != null){
            model.addAttribute("restaurantDetailDTO", restaurantDetailDTO);
        }
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<TimeOptionDTO> timeOptionDTOS = restaurantService.generateTimeOptions(restaurant_seq);
        ReservationJoinDTO reservationJoinDTO = new ReservationJoinDTO();
        // 리뷰 사진들 불러오기
        List<PictureDTO> pictureDTOS = restaurantService.selectAllPictures(restaurant_seq);
        // 메뉴 목록 조회
        List<MenuFormDTO> menuList = restaurantService.getMenuListByRestaurantSeq(restaurant_seq);

        for (MenuFormDTO menu : menuList) {
            model.addAttribute("menu_name", menu.getMenu_name());
            model.addAttribute("menu_price", menu.getMenu_price());
        }
        
        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("reservationJoinDTO",reservationJoinDTO);
        model.addAttribute("restaurantDetailDTO",restaurantDetailDTO);
        model.addAttribute("pictures",pictureDTOS);
        model.addAttribute("menuList", menuList);




        // 세션에 값이 없으면 세션 생성
        if(session.getAttribute("wantingDate") == null){
            session.setAttribute("wantingDate", restaurantService.getTodayDate());
        }
        if(session.getAttribute("wantingHour") == null){
            session.setAttribute("wantingHour", restaurantService.getNextReservationTime());
        }
        if(session.getAttribute("wantingPerson") == null){
            session.setAttribute("wantingPerson", 1);
        }
        session.setAttribute("restaurantSeq", restaurant_seq);
        return "/restaurant/restaurantInfo";
    }


    @PostMapping("/detail/{restaurant_seq}")
    public String joinReservation(@PathVariable Long restaurant_seq, @Validated ReservationJoinDTO reservationJoinDTO, BindingResult bindingResult,HttpSession session) throws ParseException {
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);

        if (bindingResult.hasErrors()) {
            log.error("오류" + bindingResult);
            return "/detail/{restaurant_seq}";
        }

        String formattedDate = restaurantService.getReservationTime(reservationJoinDTO);

        restaurantService.insertReservation(
                Reservation.builder()
                        .member_seq(member_seq)
                        .restaurant_seq(restaurant_seq)
                        .res_date(formattedDate)
                        .res_people(reservationJoinDTO.getPerson())
                        .build()
        );

        return "redirect:/members/my";
    }


    @GetMapping("/detail/{restaurant_seq}/menuList")
    public String menu(@PathVariable Long restaurant_seq, Model model, HttpSession session) {
        // 레스토랑 정보 조회
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        // 메뉴 목록 조회
        List<MenuFormDTO> menuList = restaurantService.getMenuListByRestaurantSeq(restaurant_seq);

        for (MenuFormDTO menu : menuList) {
            model.addAttribute("menu_name", menu.getMenu_name());
            model.addAttribute("menu_price", menu.getMenu_price());
        }
        //찜
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);


        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        model.addAttribute("menuList", menuList);



        return "/restaurant/menuList";
    }

    @GetMapping("/detail/{restaurant_seq}/photo")
    public String photo(@PathVariable Long restaurant_seq, Model model, HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        RestaurantDetailDTO restaurantDetailDTO = restaurantService.getRestaurantDetail(restaurant_seq);
        if(restaurantDetailDTO != null){
            model.addAttribute("restaurantDetailDTO", restaurantDetailDTO);
        }
      // 리뷰 사진들 불러오기
        List<PictureDTO> pictureDTOS = restaurantService.selectAllPictures(restaurant_seq);
      
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<TimeOptionDTO> timeOptionDTOS = restaurantService.generateTimeOptions(restaurant_seq);
        ReservationJoinDTO reservationJoinDTO = new ReservationJoinDTO();


        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("reservationJoinDTO",reservationJoinDTO);
        model.addAttribute("restaurantDetailDTO",restaurantDetailDTO);
       
        model.addAttribute("pictures",pictureDTOS);

        return "/restaurant/photo";
    }

    @GetMapping("/detail/{restaurant_seq}/review")
    public String review(@PathVariable Long restaurant_seq,Model model, HttpSession session){
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        List<ReviewStatsDTO> reviewStatsDTOS = reviewService.selectReviewCount(restaurant_seq);
        log.info("reviewDTOs: {}", reviewDTOs);


        model.addAttribute("reviewCount",reviewStatsDTOS);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        model.addAttribute("reviewDTOs", reviewDTOs);
        model.addAttribute("restaurant_seq", restaurant_seq);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        return "/restaurant/review";
    }


    @GetMapping("/review/write")
    public String writeReview(@RequestParam Long res_seq, Model model, HttpSession session){
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        Long restaurant_seq = restaurantService.getRestaurantSeqByResSeq(res_seq);
        String  access = reviewService.checkReviewData(res_seq,loginMemberSeq);
        model.addAttribute("res_seq", res_seq);
        model.addAttribute("restaurant_seq", restaurant_seq);
        if(access.equals("access granted")){
            return "/restaurant/reviewWriteForm";
        }else{
            return "redirect:/members/my";
        }
    }

    @PostMapping("/review/write")
    public String writeReview(@ModelAttribute ReviewDTO reviewDTO, HttpSession session){
        reviewService.insertReviewAndImages(reviewDTO,session);
        return "redirect:/restaurant/detail/"+reviewDTO.getRestaurant_seq()+"/review";
    }

    /*레스토랑상세페이지-공지*/
    @GetMapping("/detail/{restaurant_seq}/notice")
    public String notice(@PathVariable Long restaurant_seq, Model model, HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        RestaurantDetailDTO restaurantDetailDTO = restaurantService.getRestaurantDetail(restaurant_seq);
        if(restaurantDetailDTO != null){
            model.addAttribute("restaurantDetailDTO", restaurantDetailDTO);
        }
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<TimeOptionDTO> timeOptionDTOS = restaurantService.generateTimeOptions(restaurant_seq);
        ReservationJoinDTO reservationJoinDTO = new ReservationJoinDTO();


        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("reservationJoinDTO",reservationJoinDTO);
        model.addAttribute("restaurantDetailDTO",restaurantDetailDTO);

        return "/restaurant/notice";
    }


}
