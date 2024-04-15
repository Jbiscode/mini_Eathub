package com.eathub.mapper;

import com.eathub.dto.*;
import com.eathub.entity.Reservation;
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
    void insertReservation(Reservation reservationJoinDTO);

    void insertRestaurantStatus(RestaurantInfo restaurantJoinDTO);

    /**
     * 해당 식당에 메뉴 등록
     * @param restaurant_seq
     * @param MenuFormDTOList
     */
    void insertRestaurantMenu(@Param("restaurant_seq") Long restaurant_seq, @Param("menuFormDTOList") List<MenuFormDTO> MenuFormDTOList);
    void insertRestaurantImage(@Param("uuid") String uuid, @Param("restaurantSeq") Long restaurantSeq);

    void insertRestaurantDetail(RestaurantDetailDTO restaurantDetailDTO);


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
    OwnerRestaurantDetailDTO selectRestaurantInfoWithType(Long restaurant_seq);
    List<MyPageDTO> selectOwnerRestaurantList(Long member_seq);

    //category_seq별 레스토랑 리스트
    List<SearchResultDTO> selectSearchCategotyResultList(Long categorySeq);

    //예약 top 리스트
    List<SearchResultDTO> selectRestaurantTopSearchList();

    //오늘의 예약 리스트
    List<SearchResultDTO> selectRestaurantMonthlySearchList();

    //어디로 가시나요?
    List<SearchResultDTO> selectSearchAddressResultList(List address);

    List<SearchResultDTO> selectRestaurantTodaySearchList();

    List<SearchResultDTO> selectRandomRestaurant();

    RestaurantDetailDTO selectRestaurantDetail(Long restaurantSeq);

    Long selectRestaurantSeqByResSeq(Long res_seq);
    List<PictureDTO> selectAllPictures(Long restaurant_seq);


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

    String getRestaurantType(Long categorySeq);

    RestaurantEditDTO selectRestaurantByRestaurantSeq(Long restaurantSeq);

    void updateRestaurantInfo(RestaurantEditDTO restaurantJoinDTO);
}
