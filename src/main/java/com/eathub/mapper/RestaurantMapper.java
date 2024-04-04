package com.eathub.mapper;

import com.eathub.dto.CategoryDTO;
import com.eathub.dto.MyPageDTO;
import com.eathub.dto.SearchResultDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RestaurantMapper {
    RestaurantInfo selectRestaurantInfo(Long restaurant_seq);
    List<RestaurantInfo> selectRestaurantInfoList();

    // 검색결과에 띄울 식당 정보 조회(찜 여부 포함)
    List<SearchResultDTO> selectRestaurantSearchList();
    // 마이페이지에 띄울 식당 정보 조회
    MyPageDTO selectMyPageDTO(@Param("restaurant_seq") Long restaurant_seq, @Param("member_seq") Long member_seq);
    void updateZzimComment(@Param("zzim_seq") Long zzim_seq, @Param("comment") String comment);

    int checkZzimData(@Param("restaurant_seq") Long restaurant_seq, @Param("member_seq") Long member_seq);
    List<RestaurantZzim> selectZzimList(Long member_seq);

    void insertZzimRestaurant(RestaurantZzim restaurantZzim);

    void deleteZzimRestaurant(RestaurantZzim restaurantZzim);
    void updateZzimTotal(@Param("restaurant_seq") Long restaurant_seq, @Param("count") int count);

    List<CategoryDTO> selectCategoryList();

    void insertRestaurant(RestaurantInfo restaurantJoinDTO);
}
