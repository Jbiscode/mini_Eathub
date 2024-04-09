package com.eathub.controller;

import javax.servlet.http.HttpSession;

import com.eathub.dto.TimeOptionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.eathub.conf.SessionConf;
import com.eathub.entity.RestaurantInfo;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
        List<TimeOptionDTO> timeOptionDTOS = restaurantService.generateTimeOptions();

        
        model.addAttribute("isZzimed", restaurantService.getZzimCount(restaurant_seq, loginMemberSeq) > 0);
        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        model.addAttribute("timeOptions", timeOptionDTOS);
        return "/restaurant/restaurantInfo";
    }
    //레스토랑 메뉴리스트
    @GetMapping("/menuList/{restaurant_seq}")
    public String restaurantMenuList(@PathVariable Long restaurant_seq,Model model,HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);
        Long loginMemberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);

        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        return "/restaurant/menuList";
    }
    //레스토랑 사진
    @GetMapping("/photo/{restaurant_seq}")
    public String restaurantPhoto(@PathVariable Long restaurant_seq,Model model,HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);

        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        return "/restaurant/photo";
    }
    //레스토랑 리뷰
    @GetMapping("/review/{restaurant_seq}")
    public String restaurantReview(@PathVariable Long restaurant_seq,Model model,HttpSession session){
        RestaurantInfo selectRestaurantInfo = restaurantService.selectRestaurantInfo(restaurant_seq);

        model.addAttribute("restaurantInfo", selectRestaurantInfo);
        return "/restaurant/review";
    }

}
