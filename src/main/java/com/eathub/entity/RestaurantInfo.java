package com.eathub.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Builder
public class RestaurantInfo {
    private Long restaurant_seq;
    private Long member_seq;
    private Long category_seq;
    private String restaurant_name;
    private String tag;
    private String location;
    private String description;
    private String phone;
    private String zipcode;
    private String address1;
    private String address2;
    private Timestamp approvedAt;
    private Time openHour;
    private Time closeHour;
    private String  closedDay;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private Double rating;
    private Integer review_total;
    private Integer zzim_total;
    private String status;

    public String getRating() {
        return String.format("%.2f", rating);
    }
}
