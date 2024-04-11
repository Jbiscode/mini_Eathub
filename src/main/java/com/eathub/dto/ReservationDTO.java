package com.eathub.dto;

import com.eathub.entity.ENUM.RES_STATUS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ReservationDTO {
    private Long res_seq;
    private Long restaurant_seq;
    private Long member_seq;
    private RES_STATUS res_status;
    private Date res_log;
    private Date res_date;
    private Integer res_people;
    private String res_comment;

    private String restaurant_name;
    private String tag;

    private String dateFormat;
    private Long dDay;
    private Long absDday;

    private String image_url;
}
