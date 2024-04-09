package com.eathub.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.eathub.conf.SessionConf;
import com.eathub.entity.RestaurantInfo;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

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
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        
        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        return "/restaurant/restaurantInfo";
    }

    @GetMapping("/detail/{restaurant_seq}/menuList")
    public String menu(@PathVariable Long restaurant_seq){
        return "/restaurant/menuList";
    }

    @GetMapping("/detail/{restaurant_seq}/photo")
    public String photo(@PathVariable Long restaurant_seq){
        return "/restaurant/photo";
    }

    @GetMapping("/detail/{restaurant_seq}/review")
    public String review(@PathVariable Long restaurant_seq){
        return "/restaurant/review";
    }


}
