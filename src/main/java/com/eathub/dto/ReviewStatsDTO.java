package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewStatsDTO {
    private Integer review_count;
    private Double rating_avg;
}
