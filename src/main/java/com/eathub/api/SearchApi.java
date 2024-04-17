package com.eathub.api;

import com.eathub.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchApi {

    private final SearchService searchService;

    //현재시간 부터 자정 전까지 시간들
    @PostMapping("/getAvailableTimes/")
    public List<String> getOutdatedTimes() {
        return searchService.getAvailableTimes();
    }
}
