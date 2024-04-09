package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class RestaurantJoinDTO {

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
    @NotBlank
    private String closedDay;

    // 상세 정보 (메뉴, 공지사항 등등)
}
