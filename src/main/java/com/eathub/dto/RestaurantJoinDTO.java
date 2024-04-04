package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;

@Setter
@Getter
@ToString
public class RestaurantJoinDTO {

    private Long category_seq;
    private String restaurant_name;
    private String tag;
    private String location;
    private String description;
    private String phone;
    private String zipcode;
    private String address1;
    private String address2;
    private String openHour;
    private String closeHour;
    private String closedDay;

}
