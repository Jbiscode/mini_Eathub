package com.eathub.api;

import com.eathub.conf.SessionConf;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    // 마이페이지 찜목록에 comment 추가 API
    @PostMapping("/zzim/comment/{zzim_seq}")
    public ResponseEntity<?> zzimComment(@PathVariable("zzim_seq") Long zzim_seq,@RequestBody Map<String,Object> json) {
        String comment = (String) json.get("comment");
        restaurantService.updateZzimComment(zzim_seq, comment);
        return ResponseEntity.ok().body(Map.of(
                "success", true
        ));
    }
}
