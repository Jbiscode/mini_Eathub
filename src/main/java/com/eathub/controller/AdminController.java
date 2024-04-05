package com.eathub.controller;

import com.eathub.entity.RestaurantInfo;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final RestaurantService restaurantService;
    @GetMapping
    public String adminPage(Model model) {
        List<RestaurantInfo> restaurantInfoList = restaurantService.selectRestaurantInfoList();
        model.addAttribute("restaurantInfoList", restaurantInfoList);
        return "/admin/adminPage";
    }
}
