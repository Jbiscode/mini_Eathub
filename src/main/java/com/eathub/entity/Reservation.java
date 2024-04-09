package com.eathub.entity;

import com.eathub.entity.ENUM.RES_STATUS;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Reservation {
    private Long res_seq;
    private Long restaurant_seq;
    private Long member_seq;
    private RES_STATUS res_status;
    private Date res_log;
    private Date res_date;
    private Integer res_people;
    private String res_comment;
}
