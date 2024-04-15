package com.eathub.controller;

import com.eathub.conf.SessionConf;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.dto.SearchResultDTO;
import com.eathub.dto.TimeOptionDTO;
import com.eathub.service.RestaurantService;
import com.eathub.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
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
        // 시간태그 생성해서 가져오기
        List<TimeOptionDTO> timeOptionDTOS = searchService.generateTimeOptions();
        // 레스토랑 목록 가져오기
        List<SearchResultDTO> searchResultList = restaurantService.selectSearchResultList(member_seq);
        List<RestaurantDetailDTO> restaurantDetailDTOList = restaurantService.getRestaurantDetailList();

        try {
            for (SearchResultDTO searchResultDTO : searchResultList) {
                Long restaurant_seq = searchResultDTO.getRestaurant_seq();
                for (RestaurantDetailDTO restaurantDetailDTO : restaurantDetailDTOList) {
                    if (restaurantDetailDTO.getRestaurant_seq().equals(restaurant_seq)) {
                        searchResultDTO.setImage_url(restaurantDetailDTO.getImage_url());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("restaurantList", searchResultList);

        // 세션에 값이 없으면 세션 생성
        if(session.getAttribute("wantingDate") == null){
            session.setAttribute("wantingDate", searchService.getTodayDate());
        }
        if(session.getAttribute("wantingHour") == null){
            session.setAttribute("wantingHour", searchService.getNextReservationTime());
        }
        if(session.getAttribute("wantingPerson") == null){
            session.setAttribute("wantingPerson", 1);
        }
        return "/members/searchResult";
    }

    @GetMapping("/result")
    public String searchResult(Model model){
        List<TimeOptionDTO> timeOptionDTOS = searchService.generateTimeOptions();
        model.addAttribute("timeOptions", timeOptionDTOS);
        return "/members/searchResult";
    }
}
