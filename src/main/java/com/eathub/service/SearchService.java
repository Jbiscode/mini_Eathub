package com.eathub.service;

import com.eathub.dto.TimeOptionDTO;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchService {

    // 타임리프에 사용할 시간 옵션을 생성하는 메서드 6시부터 23시 30분까지 30분 단위로 생성
    public List<TimeOptionDTO> generateTimeOptions() {
        List<TimeOptionDTO> timeOptions = new ArrayList<>();
        LocalTime time = LocalTime.of(6, 0);
        while (!time.equals(LocalTime.of(00, 00))) {
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
}
