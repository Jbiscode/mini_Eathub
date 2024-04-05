package com.eathub.mapper;

import com.eathub.dto.CategoryDTO;
import com.eathub.dto.MyPageDTO;
import com.eathub.dto.RestaurantJoinDTO;
import com.eathub.dto.SearchResultDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RestaurantMapper {
//    INSERT
    void insertZzimRestaurant(RestaurantZzim restaurantZzim);
    void insertRestaurant(RestaurantInfo restaurantJoinDTO);
    void insertRestaurantStatus(RestaurantInfo restaurantJoinDTO);



//    SELECT
    RestaurantInfo selectRestaurantInfo(Long restaurant_seq);
    List<RestaurantInfo> selectRestaurantInfoList();
    // 검색결과에 띄울 식당 정보 조회(찜 여부 포함)
    List<SearchResultDTO> selectRestaurantSearchList();
    // 마이페이지에 띄울 식당 정보 조회
    MyPageDTO selectMyPageDTO(@Param("restaurant_seq") Long restaurant_seq, @Param("member_seq") Long member_seq);
    int checkZzimData(@Param("restaurant_seq") Long restaurant_seq, @Param("member_seq") Long member_seq);
    List<RestaurantZzim> selectZzimList(Long member_seq);
    List<CategoryDTO> selectCategoryList();
    RestaurantInfo selectRestaurant(RestaurantJoinDTO restaurantJoinDTO);

    List<MyPageDTO> selectOwnerRestaurantList(Long member_seq);



//    UPDATE
    void updateZzimComment(@Param("zzim_seq") Long zzim_seq, @Param("comment") String comment);

    /**
     * 식당 등록 상태 변경(Status)
     * @param restaurant_seq
     * @param status
     */
    void updateRestaurantStatus(@Param("restaurant_seq") Long restaurant_seq,@Param("admin_seq") Long admin_seq, @Param("status") String status, @Param("comment") String comment);

    /**
     * 식당 정보에 있는 Status 상태 변경
     * @param restaurant_seq
     * @param status
     */
    void updateRestaurantInfoStatus(@Param("restaurant_seq") Long restaurant_seq, @Param("status") String status);
    void updateZzimTotal(@Param("restaurant_seq") Long restaurant_seq, @Param("count") int count);



//    DELETE
    void deleteZzimRestaurant(RestaurantZzim restaurantZzim);

}
