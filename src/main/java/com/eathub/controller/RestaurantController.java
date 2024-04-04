package com.eathub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {
    @RequestMapping("/detail/{restaurant_seq}")
    public String restaurantInfo(@PathVariable Long restaurant_seq){
        return "/restaurant/restaurantInfo";
    }

}
