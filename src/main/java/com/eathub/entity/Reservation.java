package com.eathub.entity;

import com.eathub.entity.ENUM.RES_STATUS;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Reservation {

    private Long res_seq;
    @NotNull
    private Long restaurant_seq;
    @NotNull
    private Long member_seq;

    private RES_STATUS res_status;

    private String res_log;
    @NotBlank
    private String res_date;
    @NotNull
    private Integer res_people;

    private String res_comment;


}