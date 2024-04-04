package com.eathub.service;

import com.eathub.dto.CategoryDTO;
import com.eathub.dto.MyPageDTO;
import com.eathub.dto.RestaurantJoinDTO;
import com.eathub.dto.SearchResultDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import com.eathub.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    //   식당 정보 조회
    public List<RestaurantInfo> selectRestaurantInfoList() {
        return restaurantMapper.selectRestaurantInfoList();
    }

    /**
     * 사용자의 찜 목록에 있는 식당들을 조회하는 메서드입니다.
     *
     * @param member_seq 사용자의 고유 번호입니다. 이 번호를 통해 사용자의 찜 목록을 조회합니다.
     * @return 사용자가 찜한 식당들의 목록을 반환합니다. 각 식당은 SearchResultDTO 객체로 표현됩니다.
     *         이 객체에는 식당의 고유 번호, 이름, 위치 등의 정보와 함께 사용자가 해당 식당을 찜했는지 여부를 나타내는 isZzimed 필드가 포함됩니다.
     */
    public List<SearchResultDTO> selectSearchResultList(Long member_seq) {
        List<SearchResultDTO> searchResultList = restaurantMapper.selectRestaurantSearchList();
        // 일치하는 항목이 있으면 isZzimed 필드를 true로 설정합니다.
        for (SearchResultDTO restaurant : searchResultList) {
            restaurant.setZzimed(restaurantMapper.selectZzimList(member_seq).stream()
                    .anyMatch(zzim -> zzim.getRestaurant_seq().equals(restaurant.getRestaurant_seq())));
        }
        return searchResultList;
    }


//   유저가 찜한 식당 리스트 조회
    public List<MyPageDTO> getZzimRestaurantList(Long member_seq) {
        List<RestaurantZzim> zzimList = restaurantMapper.selectZzimList(member_seq);
        List<MyPageDTO> restaurantInfoList = new ArrayList<>();
        for (RestaurantZzim zzim : zzimList) {
            restaurantInfoList.add(restaurantMapper.selectMyPageDTO(zzim.getRestaurant_seq(), member_seq));
        }
        return restaurantInfoList;
    }

//    찜 추가 및 삭제
    @Transactional
    public boolean toggleZzimRestaurant(Long member_seq, Long restaurant_seq) {

        RestaurantZzim zzim = RestaurantZzim.builder()
                .member_seq(member_seq)
                .restaurant_seq(restaurant_seq)
                .build();

        RestaurantZzim zzimResult =restaurantMapper.selectZzimList(member_seq).stream()
                .filter(zzim1 -> zzim1.getRestaurant_seq().equals(restaurant_seq))
                .findFirst()
                .orElse(null);

        log.info("zzimResult: {}", zzimResult);
        if (zzimResult == null) {
            restaurantMapper.insertZzimRestaurant(zzim);
            return true;
        } else {
            restaurantMapper.deleteZzimRestaurant(zzim);
            return false;
        }
    }

    // 마이페이지 찜목록에 comment 추가
    public void updateZzimComment(Long zzim_seq, String comment) {
        restaurantMapper.updateZzimComment(zzim_seq, comment);
    }


    public Map<String ,String> getLocationList(){
        Map<String, String> locations = new LinkedHashMap<>();
        locations.put("SEOUL", "서울");
        locations.put("BUSAN", "부산");
        locations.put("JEJU", "제주");
        return locations;
    }

    public List<CategoryDTO> getCategoryList(){
        return restaurantMapper.selectCategoryList();
    }


    public void insertRestaurant(RestaurantInfo restaurantJoinDTO) {
        restaurantMapper.insertRestaurant(restaurantJoinDTO);
    }


    public RestaurantInfo selectSavedRestaurant(RestaurantJoinDTO restaurantJoinDTO) {

        return restaurantMapper.selectRestaurant(restaurantJoinDTO);
    }

    public void insertRestaurantStatus(RestaurantInfo restaurantJoinDTO) {
        restaurantMapper.insertRestaurantStatus(restaurantJoinDTO);
    }
}
