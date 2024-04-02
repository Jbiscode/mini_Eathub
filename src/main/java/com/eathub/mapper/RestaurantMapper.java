package com.eathub.mapper;

import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;

import java.util.List;

public interface RestaurantMapper {
    RestaurantInfo selectRestaurantInfo(Long restaurant_seq);

    List<RestaurantZzim> selectZzimList(Long member_seq);

    void insertZzimRestaurant(RestaurantZzim restaurantZzim);

    void deleteZzimRestaurant(RestaurantZzim restaurantZzim);
}
