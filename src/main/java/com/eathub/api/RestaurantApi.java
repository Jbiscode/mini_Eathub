package com.eathub.api;

import com.eathub.conf.SessionConf;
import com.eathub.dto.OwnerRestaurantDetailDTO;
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
import java.util.List;
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

    // 점주의 내 가게 리스트에서 가게 선택하기 API
    @PostMapping("/owner/{restaurantSeq}")
    public ResponseEntity<?> selectOwnerRestaurant(@PathVariable("restaurantSeq") Long restaurant_seq) {
        try {
            OwnerRestaurantDetailDTO restaurantInfo = restaurantService.selectRestaurantInfoWithType(restaurant_seq);
            log.info("restaurantInfo = {}", restaurantInfo);

            return ResponseEntity.ok().body(Map.of(
                        "restaurantInfo" , restaurantInfo
                ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("오류 메시지");
        }
    }

    @PostMapping("/admin/updateRestaurantStatus")
    public ResponseEntity<?> updateRestaurantStatus(@RequestBody Map<String, Object> json) {
        Long restaurant_seq = Long.parseLong(json.get("restaurantSeq").toString());
        String  status= (String) json.get("status");
        restaurantService.updateRestaurantStatus(restaurant_seq, 1L,status,null);
        return ResponseEntity.ok().body(Map.of(
                "success", true
        ));
    }
    //오늘이라면 오픈시간부터 현재시간 전까지의 timeOption 들을 HHmm 형식으로 반환
    @PostMapping("/getOutdatedTimes/{restaurantSeq}")
    public List<String> getOutdatedTimes(@PathVariable("restaurantSeq") Long restaurantSeq) {
        return restaurantService.getOutdatedTime(restaurantSeq);
    }
    //선택한 날짜 중 예약이 된 시간들을 HHmm 형식으로 반환.
    @PostMapping("/getBookedTimes/{restaurantSeq}/{memberSeq}/{selectedDate}")
    public List<String> getBookedTimes(@PathVariable("restaurantSeq") Long restaurant_seq,
                                       @PathVariable("memberSeq") Long memberSeq,
                                       @PathVariable("selectedDate") String selectedDate
                                       ) {
        return restaurantService.getBookedTimes(restaurant_seq, memberSeq, selectedDate);
    }
}
