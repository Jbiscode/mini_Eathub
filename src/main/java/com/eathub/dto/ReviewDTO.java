package com.eathub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class ReviewDTO {
    private Long res_seq;
    private Long restaurant_seq;
    private Long member_seq;
    // 존재하는 리뷰가 있나
    private String existing_review;

    // 리뷰 작성시
    private Double rating;
    private String content;
    private List<CommonsMultipartFile> reviewImages;
    private List<String> pictureUrls;

}
