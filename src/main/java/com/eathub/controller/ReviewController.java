package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.entity.ENUM.MEMBER_TYPE;
import com.eathub.service.RestaurantService;
import com.eathub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;

    @GetMapping("")
    public String review(Model model, HttpSession session){
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        MEMBER_TYPE mem_type = (MEMBER_TYPE) session.getAttribute(SessionConf.LOGIN_MEMBER_TYPE);


        model.addAttribute("member_seq", member_seq);


        if(mem_type.equals(MEMBER_TYPE.OWNER)){
            Long total_review = reviewService.ownerTotalReviewCount(member_seq);

            model.addAttribute("total_review", total_review);
            return "/members/ownerReview";

        }

        Long total_review = reviewService.totalReviewCount(member_seq);

        model.addAttribute("total_review", total_review);
        return "/members/myReview";

    }


}
