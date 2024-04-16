package com.eathub.api;


import com.eathub.conf.SessionConf;
import com.eathub.dto.ReservationDTO;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.service.MemberService;
import com.eathub.service.RestaurantService;
import com.eathub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReservationApi {

    private final MemberService memberService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @GetMapping("/get/reservations")
    public ResponseEntity<?> getReservations(@RequestParam("page") int page, @RequestParam("type") int type_tab, HttpSession session){

        Long member_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);
        List<ReservationDTO> reservationList =  memberService.getReservationListPage(member_seq, (page-1)*5, type_tab);
        List<RestaurantDetailDTO> restaurantDetailList = restaurantService.getRestaurantDetailList();

        if(reservationList != null){
            for (ReservationDTO reservationDTO : reservationList) {
                Long restaurant_seq = reservationDTO.getRestaurant_seq();
                String isExistingReview = reviewService.checkReviewData(reservationDTO.getRes_seq(),reservationDTO.getMember_seq());

                if(isExistingReview.equals("access granted")){
                    reservationDTO.setReviewed(false);
                }else{
                    reservationDTO.setReviewed(true);
                }

                for(RestaurantDetailDTO restaurantDetailDTO : restaurantDetailList){
                    if(restaurantDetailDTO.getRestaurant_seq().equals(restaurant_seq)){
                        reservationDTO.setImage_url(restaurantDetailDTO.getImage_url());
                        break;
                    }
                }
            }
            return ResponseEntity.ok(reservationList);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/post/reservations/cancel")
    public ResponseEntity<?> reservationCancel(@RequestParam("res_seq") Long res_seq, HttpSession session){

        // 세션에서 mem_seq받아서 예약자의 mem_seq와 비교함
        Long mem_seq = (Long) session.getAttribute(SessionConf.LOGIN_MEMBER_SEQ);

        // res_seq 가지고 reservation 테이블의 res_status => REJECT로 변경
        // 성공시 SUCCESS , 실패시 FAIL 값 반환
        String result = memberService.cancelReservation(res_seq, mem_seq);

        if ("SUCCESS".equals(result)) {
            // 성공 응답
            return ResponseEntity.ok().body(Map.of("message", "예약이 취소되었습니다."));
        }

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(Map.of("message", "예약은 3일 전까지만 취소할 수 있습니다."));

    }
}
