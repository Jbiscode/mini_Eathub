package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
@Getter
@Setter
public class SearchResultDTO {
    private Long restaurant_seq;
    private Long member_seq;
    private Long category_seq;

    private String restaurant_name;
    private String tag;
    private String location;
    private String description;

    private Time openHour;
    private Time closeHour;

    private Double rating;
    private Integer review_total;
    private Integer zzim_total;

    private boolean isZzimed;

    private String image_url;

    public String getRating() {
        return String.format("%.2f", rating);
    }
}
