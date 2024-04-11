package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class OwnerRestaurantDetailDTO {
    private Long restaurant_seq;
    private Long member_seq;
    private String restaurant_type;
    private String restaurant_name;
    private String tag;
    private String location;
    private String description;
    private String phone;
    private String zipcode;
    private String address1;
    private String address2;

    private Time openHour;
    private Time closeHour;
    private String  closedDay;

    private Double rating;
    private Integer review_total;
    private Integer zzim_total;
    private String status;

    private String img_url;
}
