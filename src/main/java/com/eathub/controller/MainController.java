package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.dto.SearchResultDTO;
import com.eathub.dto.TimeOptionDTO;
import com.eathub.service.RestaurantService;
import com.eathub.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SearchService searchService;
    private final RestaurantService restaurantService;
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<SearchResultDTO> searchResultList = restaurantService.selectSearchResultList(member_seq);
        model.addAttribute("restaurantList", searchResultList);
        return "index";
    }

    @GetMapping("/search/category/{category_seq}")
    public String category(@PathVariable Long category_seq, Model model, HttpSession session){
        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<TimeOptionDTO> timeOptionDTOS = searchService.generateTimeOptions();
        List<SearchResultDTO> searchResultList = restaurantService.selectSearchCategotyResultList(member_seq, category_seq);
        for (SearchResultDTO searchResultDTO : searchResultList) {
            Long restaurant_seq = searchResultDTO.getRestaurant_seq();
            RestaurantDetailDTO restaurantDetailDTO = restaurantService.getRestaurantDetail(restaurant_seq);
            if(restaurantDetailDTO != null){
                searchResultDTO.setImage_url(restaurantDetailDTO.getImage_url());
            }
        }
        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("restaurantList", searchResultList);
        return "/members/searchResult";
    }
}
