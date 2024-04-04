package com.eathub.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    private Long category_seq;
    private String restaurant_type;
}
