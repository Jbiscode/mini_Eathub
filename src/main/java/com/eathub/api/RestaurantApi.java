package com.eathub.api;

import com.eathub.conf.SessionConf;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantApi {
    private final RestaurantService restaurantService;

//    찜 추가 및 삭제 API
    @PostMapping("/zzim/{restaurant_id}")
    public ResponseEntity<?> zzim(@PathVariable("restaurant_id") Long restaurant_seq, HttpSession session) {
        Long memberSeq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        log.info("memberSeq: {}", memberSeq);
        boolean isAdded = restaurantService.toggleZzimRestaurant(memberSeq, restaurant_seq);
        log.info("isAdded: {}", isAdded);
        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "isBookmarked", isAdded
        ));
    }
}
