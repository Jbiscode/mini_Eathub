package com.eathub.service;

import com.eathub.dto.*;

import com.eathub.entity.Reservation;
import com.eathub.entity.RestaurantInfo;
import com.eathub.entity.RestaurantZzim;
import com.eathub.mapper.ObjectStorageMapper;
import com.eathub.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public void insertReservation(Reservation reservationJoinDTO) {
        restaurantMapper.insertReservation(reservationJoinDTO);
    }

    public String getReservationTime(ReservationJoinDTO reservationJoinDTO) throws ParseException {
        // date 형으로 형변환
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HHmm");
        String date = reservationJoinDTO.getDate();
        String time = reservationJoinDTO.getHour();

        // 문자열을 Date 객체로 파싱
        Date parsedDate = format.parse(date + " " + time);

        // 다른 형식으로 변환할 SimpleDateFormat 객체 생성
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Date 객체를 다른 형식으로 변환
        return outputFormat.format(parsedDate);
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

    //카테고리별 레스토랑 조회
    public List<SearchResultDTO> selectSearchCategotyResultList(Long member_seq, Long category_seq) {
        List<SearchResultDTO> searchResultList = restaurantMapper.selectSearchCategotyResultList(category_seq);
        // 일치하는 항목이 있으면 isZzimed 필드를 true로 설정합니다.
        for (SearchResultDTO restaurant : searchResultList) {
            restaurant.setZzimed(restaurantMapper.selectZzimList(member_seq).stream()
                    .anyMatch(zzim -> zzim.getRestaurant_seq().equals(restaurant.getRestaurant_seq())));
        }
        return searchResultList;
    }


    // 타임리프에 사용할 시간 옵션을 생성하는 메서드 6시부터 23시 30분까지 30분 단위로 생성
    public List<TimeOptionDTO> generateTimeOptions(Long restaurant_seq) {
        RestaurantInfo restaurantinfo = restaurantMapper.selectRestaurantInfo(restaurant_seq);
        LocalTime openTime = restaurantinfo.getOpenHour().toLocalTime();
        LocalTime closeTime = restaurantinfo.getCloseHour().toLocalTime();
        LocalTime time = openTime;
        List<TimeOptionDTO> timeOptions = new ArrayList<>();
        while (!time.equals(closeTime)) {
            TimeOptionDTO option = new TimeOptionDTO();
            option.setTime(time.toString());
            timeOptions.add(option);
            time = time.plusMinutes(30);
        }
        return timeOptions;
    }


    //session 초기값에 저장할 오늘 날짜 구하기.
    public String getTodayDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //session 초기값에 저장할 가장 가까운 예약 가능 시간 구하기.
    public String getNextReservationTime(){
        // 현재 시간 가져오기
        LocalTime currentTime = LocalTime.now();

        // 다음 30분 단위의 예약 가능한 시간 계산
        int currentMinute = currentTime.getMinute();
        int nextReservationMinute = ((currentMinute / 30) + 1) * 30; // 다음 예약 가능한 분
        LocalTime nextReservationTime;

        if (nextReservationMinute == 60) {
            nextReservationTime = currentTime.plusHours(1).withMinute(0); // 정시로 설정
        } else {
            nextReservationTime = currentTime.withMinute(nextReservationMinute);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

        // 예약 가능한 시간 출력
        return nextReservationTime.format(formatter);
    }
    //open 시간부터 현재시간 전까지
    public List<String> getOutdatedTime(Long restaurant_seq) {
        //현재 시간 구하기
        LocalTime currentTime = LocalTime.now();
        int currentMinute = currentTime.getMinute();
        int nextReservationMinute = ((currentMinute / 30) + 1) * 30; // 다음 예약 가능한 분
        LocalTime nextReservationTime;
        if (nextReservationMinute == 60) {
            nextReservationTime = currentTime.plusHours(1).withMinute(0); // 정시로 설정
        } else {
            nextReservationTime = currentTime.withMinute(nextReservationMinute);
        }

        //저장할 데이터의 형태
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        RestaurantInfo restaurantinfo = restaurantMapper.selectRestaurantInfo(restaurant_seq);
        LocalTime time = restaurantinfo.getOpenHour().toLocalTime();
        List<String> outdatedTimes = new ArrayList<>();
        while (!time.format(formatter2).equals(nextReservationTime.format(formatter2))) {
            outdatedTimes.add(time.format(formatter));
            time = time.plusMinutes(30);
        }
        return outdatedTimes;
    }

    //회원번호, 식당번호, 지정된날짜정보에 해당되는 예약의 시간정보를 HHmm 형식으로 반환한다.
    public List<String> getBookedTimes(Long restaurantSeq, Long memberSeq, String selectedDate) {
        //회원번호, 식당번호에 해당하는 예약을 전부 뽑기
        List<Reservation> reservations = restaurantMapper.selectOnesReservation(restaurantSeq, memberSeq);

        //yyyy-MM-dd HH:mm:ss 형식으로 되어있는 각각의 시간정보가  yyyy-MM-dd 형식의 selectedDate를 포함하는지 보자.
        List<String> bookedTimes = new ArrayList<>();
        for (Reservation reservation : reservations) {
            String[] dateParts = reservation.getRes_date().split(" ");
            if(dateParts[0].equals(selectedDate)){
                String[] timeParts = dateParts[1].split(":");
                bookedTimes.add(timeParts[0] + timeParts[1]);
            }
        }
        return bookedTimes;
    }
}
