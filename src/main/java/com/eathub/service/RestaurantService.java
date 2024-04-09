package com.eathub.service;

import com.eathub.dto.TimeOptionDTO;
import com.eathub.dto.CategoryDTO;
import com.eathub.dto.MenuFormDTO;
import com.eathub.dto.MenuFormDTOWrapper;
import com.eathub.dto.MyPageDTO;
import com.eathub.dto.OwnerRestaurantDetailDTO;
import com.eathub.dto.RestaurantJoinDTO;
import com.eathub.dto.SearchResultDTO;

import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import com.eathub.mapper.ObjectStorageMapper;
import com.eathub.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final ObjectStorageMapper objectStorageMapper;

    public RestaurantInfo selectRestaurantInfo(Long restaurant_seq) {
        return restaurantMapper.selectRestaurantInfo(restaurant_seq);
    }

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
    /**
        * 찜한 식당 목록을 가져오는 메소드입니다.
        *
        * @param member_seq 회원 번호
        * @return 찜한 식당 정보 목록
        */
    public List<MyPageDTO> getZzimRestaurantList(Long member_seq) {
        List<RestaurantZzim> zzimList = restaurantMapper.selectZzimList(member_seq);
        List<MyPageDTO> restaurantInfoList = new ArrayList<>();
        for (RestaurantZzim zzim : zzimList) {
            restaurantInfoList.add(restaurantMapper.selectMyPageDTO(zzim.getRestaurant_seq(), member_seq));
        }
        return restaurantInfoList;
    }
    public int getZzimCount(Long restaurant_seq, Long member_seq) {
        return restaurantMapper.checkZzimData(restaurant_seq, member_seq);
    }

//    찜 추가 및 삭제
    /**
     * 회원의 찜한 식당을 토글하는 메소드입니다.
     *
     * @param member_seq 회원 번호
     * @param restaurant_seq 식당 번호
     * @return 찜 상태가 변경되었는지 여부를 나타내는 boolean 값
     */
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
            restaurantMapper.updateZzimTotal(restaurant_seq,1);
            return true;
        } else {
            restaurantMapper.deleteZzimRestaurant(zzim);
            restaurantMapper.updateZzimTotal(restaurant_seq,-1);
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

    public void insertRestaurantMenu(Long restaurant_seq, MenuFormDTOWrapper menuListWrapper) {
        List<MenuFormDTO> menuForm = menuListWrapper.getMenuList();
        restaurantMapper.insertRestaurantMenu(restaurant_seq, menuForm);
    }

    public RestaurantInfo selectSavedRestaurant(RestaurantJoinDTO restaurantJoinDTO) {

        return restaurantMapper.selectRestaurant(restaurantJoinDTO);
    }

    public void insertRestaurantStatus(RestaurantInfo restaurantJoinDTO) {
        restaurantMapper.insertRestaurantStatus(restaurantJoinDTO);
    }

    public List<MyPageDTO> getOwnerRestaurantList(Long member_seq) {
        return restaurantMapper.selectOwnerRestaurantList(member_seq);
    }
    /**
     * 식당의 상태를 업데이트하는 메서드입니다.
     *
     * @param restaurant_seq 업데이트하려는 식당의 고유 번호입니다.
     * @param status 식당에 설정하려는 새로운 상태입니다.
     * 이 메서드는 두 단계로 작동한다.
     * 먼저, restaurantMapper의 updateRestaurantStatus 메서드를 호출하여 식당의 상태를 업데이트합니다.
     * 그 다음, restaurantMapper의 updateRestaurantInfoStatus 메서드를 호출하여 식당 정보의 상태도 업데이트합니다.
     * 이 두 메서드는 각각 식당 테이블과 식당 정보 테이블에서 해당 식당의 상태를 업데이트하는 SQL 쿼리를 실행합니다.
     * 따라서 이 메서드는 식당과 관련된 두 테이블의 상태를 동시에 업데이트하므로, 데이터의 일관성을 유지하는 데 도움이 됨
     */
    public void updateRestaurantStatus(Long restaurant_seq,Long admin_seq, String status, String comment) {
        restaurantMapper.updateRestaurantStatus(restaurant_seq,admin_seq, status ,comment);
        restaurantMapper.updateRestaurantInfoStatus(restaurant_seq, status);
    }

    public String getRestaurantType(Long categorySeq) {
        return restaurantMapper.getRestaurantType(categorySeq);
    }
    public OwnerRestaurantDetailDTO selectRestaurantInfoWithType(Long restaurant_seq) {
        return restaurantMapper.selectRestaurantInfoWithType(restaurant_seq);
    }


    // 타임리프에 사용할 시간 옵션을 생성하는 메서드 6시부터 23시 30분까지 30분 단위로 생성
    public List<TimeOptionDTO> generateTimeOptions() {
        List<TimeOptionDTO> timeOptions = new ArrayList<>();
        LocalTime time = LocalTime.of(6, 0);
        while (!time.equals(LocalTime.of(23, 30))) {
            TimeOptionDTO option = new TimeOptionDTO();
            option.setTime(time.toString());
            timeOptions.add(option);
            time = time.plusMinutes(30);
        }
        return timeOptions;
    }


}
