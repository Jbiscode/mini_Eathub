package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.SearchResultDTO;
import com.eathub.dto.TimeOptionDTO;
import com.eathub.service.RestaurantService;
import com.eathub.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RestaurantService restaurantService;
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<SearchResultDTO> searchResultList = restaurantService.selectSearchResultList(member_seq);
        model.addAttribute("restaurantList", searchResultList);
        return "index";
    }
}
