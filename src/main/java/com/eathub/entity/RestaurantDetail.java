package com.eathub.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDetail {

    private Long restaurant_seq;
    private String image_url;
    private String description_detail;
    private String openhour_detail;
    private String precaution;
    private String amenities;
    private String parking;
    private String valet;
    private String drink;
    private String kidzone;
    private String room;

}
