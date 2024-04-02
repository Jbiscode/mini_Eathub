package com.eathub.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class RestaurantZzim {
    private Integer zzim_seq;
    private Long member_seq;
    private Long restaurant_seq;
    private Date zzim_date;
}
