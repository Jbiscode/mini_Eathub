package com.eathub.service;

import com.eathub.entity.RestaurantInfo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
public class RestaurantServiceTest {

    private final RestaurantService restaurantService;
    @Test
    @Transactional
    void insertRestaurant(){
//        restaurantService.insertRestaurant(
//                RestaurantInfo.builder()
//                        .member_seq(4L)
//                        .category_seq(1L)
//                        .restaurant_name("맛집")
//                        .tag("음식점,라면")
//                        .location("서울")
//                        .description("맛있게드세요")
//                        .phone("010-0000-0000")
//                        .zipcode("안녕")
//                        .address1("서울시")
//                        .address2("상세주소")
//                        .openHour("09:00")
//                        .closeHour(restaurantJoinDTO.getCloseHour())
//                        .closedDay(restaurantJoinDTO.getClosedDay())
//                        .build()
//        );
    }
}
