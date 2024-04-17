package com.eathub.dto;

import com.eathub.entity.ENUM.RES_STATUS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ToString
public class ReservationJoinDTO {

    @NotNull
    private Long member_seq;
    @NotBlank
    private String date;
    @NotBlank
    private String hour;
    @NotNull
    private Integer person;
    private List<String> closedDayList;

}
