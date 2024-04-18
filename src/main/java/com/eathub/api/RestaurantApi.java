package com.eathub.api;

import com.eathub.conf.SessionConf;
import com.eathub.dto.OwnerRestaurantDetailDTO;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.dto.TimeOptionDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.mapper.RestaurantMapper;
import com.eathub.service.MemberService;
import com.eathub.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantApi {
    private final RestaurantService restaurantService;
    private final MemberService memberService;
    private final RestaurantMapper restaurantMapper;

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
            restaurantInfo.setClosedDayList(memberService.convertStringToList(restaurantInfo.getClosedDay()));
            RestaurantDetailDTO restaurantDetailDTO = restaurantService.getRestaurantDetail(restaurant_seq);
            restaurantInfo.setIsDetailJoined(false);

            if(restaurantDetailDTO != null){ // null 이 아니다? => 있다
                restaurantInfo.setImg_url(restaurantDetailDTO.getImage_url());
                restaurantInfo.setIsDetailJoined(true);
            }
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





    //0000부터 2330까지 와 그에 따른 상태 반환 ,  가능 : 0, 예약됨 : 1, 지난 시간 : 2, 오픈하지 않음 : 3
    @PostMapping("/getTimeStatuses/{restaurantSeq}/{selectedDate}")
    public Map<String, Integer>  timeStatuses(@PathVariable("restaurantSeq") Long restaurant_seq,
                                       @PathVariable("selectedDate") String selectedDate
    ) {

        RestaurantInfo restaurantInfo = restaurantMapper.selectRestaurantInfo(restaurant_seq);
        LocalTime openHour = restaurantInfo.getOpenHour().toLocalTime();
        LocalTime closeHour = restaurantInfo.getCloseHour().toLocalTime();
        LocalTime nextReservationTime =  restaurantService.getNextReservationTime();

        Map<String, Integer> timeStatuses = new HashMap<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HHmm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        LocalTime nowTime = LocalTime.of(0, 0);

        //if 문의 순서에 따라 우선순위 선택 가능
        do {
            // 가능 : 0
            timeStatuses.put(nowTime.format(formatter1), 0);
            //지난 시간 : 2
            if(nowTime.isBefore(nextReservationTime) && selectedDate.equals(today.format(formatter2))){
                timeStatuses.put(nowTime.format(formatter1), 2);
            }
            //오픈하지 않음 : 3
            if(nowTime.isBefore(openHour) || nowTime.isAfter(closeHour)){
                timeStatuses.put(nowTime.format(formatter1), 3);
            }
            nowTime = nowTime.plusMinutes(30);
        } while (!nowTime.equals(LocalTime.of(0, 0)));

        if(selectedDate != null){
            // 예약됨 : 1
            List<LocalTime> BookedTimes =  restaurantService.getBookedTimes(restaurant_seq, selectedDate);
            for (LocalTime BookedTime : BookedTimes) {
                timeStatuses.put(BookedTime.format(formatter1), 1);
            }
        }
        return timeStatuses;

    }
}
