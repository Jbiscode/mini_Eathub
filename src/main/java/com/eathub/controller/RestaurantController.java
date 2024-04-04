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

    @GetMapping("/detail/{restaurant_seq}")
    public String restaurantInfo(@PathVariable Long restaurant_seq,Model model,HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        
        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        return "/restaurant/restaurantInfo";
    }

}
