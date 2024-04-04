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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    private final RestaurantService restaurantService;
    @ModelAttribute("page")
    public String page() {
        return "search";
    }
    @GetMapping("")
    public String search(Model model, HttpSession session){
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<TimeOptionDTO> timeOptionDTOS = searchService.generateTimeOptions();
        List<SearchResultDTO> searchResultList = restaurantService.selectSearchResultList(member_seq);
        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("restaurantList", searchResultList);
        return "/members/searchResult";
    }

    @GetMapping("/result")
    public String searchResult(Model model){
        List<TimeOptionDTO> timeOptionDTOS = searchService.generateTimeOptions();
        model.addAttribute("timeOptions", timeOptionDTOS);
        return "/members/searchResult";
    }
}
