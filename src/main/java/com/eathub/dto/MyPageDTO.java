package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class MyPageDTO {
    private Long zzim_seq;
    private Long member_seq;
    private Long restaurant_seq;
    private String restaurant_name;

    private String tag;
    private String description;

    private Time openHour;
    private Time closeHour;

    private Double rating;
    private Integer review_total;

    private String comment;

    private String image_url;
}
