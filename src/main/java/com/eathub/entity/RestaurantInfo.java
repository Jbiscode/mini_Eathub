package com.eathub.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Builder
public class RestaurantInfo {
    private Integer restaurant_id;
    private Long member_seq;
    private Long category_id;
    private String restaurant_name;
    private String address;
    private String description;
    private String phone;
    private Timestamp approvedAt;
    private Time openHour;
    private Time closeHour;
    private String  closedDay;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private Integer review_total;
    private Integer like_total;
    private Integer zzim_total;
}
