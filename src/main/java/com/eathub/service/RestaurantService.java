package com.eathub.service;

import com.eathub.dto.CategoryDTO;
import com.eathub.dto.MyPageDTO;
import com.eathub.dto.RestaurantJoinDTO;
import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import com.eathub.mapper.RestaurantMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

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
}
