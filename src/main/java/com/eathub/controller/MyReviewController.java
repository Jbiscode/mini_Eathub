package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.ReservationDTO;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.dto.ReviewDTO;
import com.eathub.dto.ReviewStatsDTO;
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

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;

    @GetMapping("")
    public String review(Model model, HttpSession session){
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);

        model.addAttribute("member_seq", member_seq);
        return "/members/myReview";
    }


}
