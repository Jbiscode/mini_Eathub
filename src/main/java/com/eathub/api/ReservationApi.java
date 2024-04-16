package com.eathub.api;


import com.eathub.conf.SessionConf;
import com.eathub.dto.ReservationDTO;
import com.eathub.dto.RestaurantDetailDTO;
import com.eathub.service.MemberService;
import com.eathub.service.RestaurantService;
import com.eathub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/get/reservations")
public class ReservationApi {

    private final MemberService memberService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @GetMapping("")
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
}
