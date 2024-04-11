package com.eathub.dto;

import com.eathub.entity.ENUM.RES_STATUS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class ReservationJoinDTO {

    @NotBlank
    private String date;
    @NotBlank
    private String hour;
    @NotNull
    private Integer person;

}
