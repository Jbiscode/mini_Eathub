package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ToString
@Setter
@Getter
public class RestaurantEditDTO {

    @NotNull
    private Long restaurant_seq;
    @NotNull
    private Long category_seq;
    @NotBlank
    private String restaurant_name;
    @NotBlank
    private String tag;
    @NotBlank
    private String location;
    @NotBlank
    private String description;
    @NotBlank
    private String phone;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String address1;
    @NotBlank
    private String address2;
    @NotBlank
    private String openHour;
    @NotBlank
    private String closeHour;

    private String closedDay;
    private List<String> closedDayList;

    public String getOpenHour() {
        if (this.openHour != null && !this.openHour.isEmpty()) {
            try {
                // "HH:mm:ss" 형식의 문자열을 Date 객체로 파싱
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
                Date date = parser.parse(this.openHour);

                // Date 객체를 "HH:mm" 형식의 문자열로 포맷
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                return formatter.format(date);
            } catch (Exception e) {
                // 파싱 또는 포맷 중 오류가 발생한 경우 예외 처리
                return this.openHour; // 변환할 수 없는 경우 원래 값을 그대로 반환
            }
        }
        return null;
    }

    public String getCloseHour() {
        if (this.closeHour != null && !this.closeHour.isEmpty()) {
            try {
                // "HH:mm:ss" 형식의 문자열을 Date 객체로 파싱
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
                Date date = parser.parse(this.closeHour);

                // Date 객체를 "HH:mm" 형식의 문자열로 포맷
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                return formatter.format(date);
            } catch (Exception e) {
                // 파싱 또는 포맷 중 오류가 발생한 경우 예외 처리
                return this.closeHour; // 변환할 수 없는 경우 원래 값을 그대로 반환
            }
        }
        return null;
    }
}
