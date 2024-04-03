package com.eathub.service;

import com.eathub.dto.TimeOptionDTO;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

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
