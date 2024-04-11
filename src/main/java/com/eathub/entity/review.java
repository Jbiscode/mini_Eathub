package com.eathub.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class review {
    private Long res_seq;
    private Double rating;
    private String content;
    private String reviewPicUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;
}
