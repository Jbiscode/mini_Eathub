package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.ReservationDTO;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.dto.ReviewDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.service.MemberService;
import com.eathub.service.RestaurantService;
import com.eathub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/myreview")
@RequiredArgsConstructor
public class MyReviewController {

    private final MemberService memberService;
    private final ReviewService reviewService;
    private final RestaurantService restaurantService;

    @GetMapping("")
    public String myReview(Model model, HttpSession session) {
        // 로그인회원의 예약목록 불러오기위해서 session에 있는 memseq 가져오기
        Long mem_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        log.info("mem_seq={}", mem_seq);

        // mem_seq로 예약상태 리스트 불러오기 (reservation Service? 일단 memberservice에)
        List<ReservationDTO> reservationList =  memberService.getReservationList(mem_seq);

        for (ReservationDTO reservationDTO : reservationList) {
            Long restaurant_seq = reservationDTO.getRestaurant_seq();
            RestaurantDetailDTO restaurantDetailDTO = restaurantService.getRestaurantDetail(restaurant_seq);
            reservationDTO.setImage_url(restaurantDetailDTO.getImage_url());
        }
        model.addAttribute("reservationDTOList", reservationList);


        // 예약이 존재하면 reservationDTO의 isReviewed true로 아니면 false
        for (ReservationDTO reservationDTO : reservationList) {
            String isExistingReview = reviewService.checkReviewData(reservationDTO.getRes_seq(),reservationDTO.getMember_seq());
            if(isExistingReview.equals("access granted")){
                reservationDTO.setReviewed(false);
            }else{
                reservationDTO.setReviewed(true);
            }
            log.info("reservationDTO = {} ", reservationDTO);
        }


        model.addAttribute("reservationDTOList", reservationList);
        return "/members/myReview";
    }


}
