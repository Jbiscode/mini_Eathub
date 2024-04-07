package com.eathub.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantStatus {
    private Long request_seq;
    private Long restaurant_seq;
    private Long admin_seq;
    private String status;
    private String comment;
}
