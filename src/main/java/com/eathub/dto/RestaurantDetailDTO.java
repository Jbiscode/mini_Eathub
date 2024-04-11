package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Setter
@Getter
@ToString
public class RestaurantDetailDTO {
    private Long restaurant_seq;
    private String image_url;
    private String description_detail;
    private String openhour_detail;
    private String precaution;
    private String amenities;
    private String parking;
    private String valet;
    private String drink;
    private String kidszone;
    private String room;


    private CommonsMultipartFile restaurant_image;
}
