package com.eathub.mapper;

import com.eathub.dto.MyPageDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RestaurantMapper {
    RestaurantInfo selectRestaurantInfo(Long restaurant_seq);

    // 마이페이지에 띄울 식당 정보 조회
    MyPageDTO selectMyPageDTO(@Param("restaurant_seq") Long restaurant_seq, @Param("member_seq") Long member_seq);
    void updateZzimComment(@Param("zzim_seq") Long zzim_seq, @Param("comment") String comment);

    List<RestaurantZzim> selectZzimList(Long member_seq);

    void insertZzimRestaurant(RestaurantZzim restaurantZzim);

    void deleteZzimRestaurant(RestaurantZzim restaurantZzim);
}
