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
import java.text.SimpleDateFormat;
import java.util.Date;
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
        // 시간태그 생성해서 가져오기
        List<TimeOptionDTO> timeOptionDTOS = searchService.generateTimeOptions();
        // 레스토랑 목록 가져오기
        List<SearchResultDTO> searchResultList = restaurantService.selectSearchResultList(member_seq);
        model.addAttribute("timeOptions", timeOptionDTOS);
        model.addAttribute("restaurantList", searchResultList);

        Date date = new Date();

        // "yyyy-MM-dd" 형식으로 날짜를 포맷팅합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hhmm");

        // 포맷팅된 날짜 문자열을 생성합니다.
        String today = sdf.format(date);
        String strHour = sdf2.format(date);
        int realHour = Integer.parseInt(strHour);
//        int hour = realHour /

        // 세션에 값이 없으면 세션 생성
        if(session.getAttribute("wantingDate") != null){
            session.setAttribute("wantingDate", "");
            session.setAttribute("wantingHour", "");
            session.setAttribute("wantingPerson", "");
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
