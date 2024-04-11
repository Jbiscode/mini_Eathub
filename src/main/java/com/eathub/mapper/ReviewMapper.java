package com.eathub.mapper;

import com.eathub.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    //    INSERT
    /**
     * 리뷰 등록
     * @param reviewDTO
     */
    void insertReview(ReviewDTO reviewDTO);

    /**
     * 리뷰 이미지 등록
     * @param reviewDTO
     */
    void insertReviewImages(ReviewDTO reviewDTO);
    //   SELECT
    /**
     * 해당 예약의 리뷰가 존재하는지 확인
     * @param res_seq
     */
    ReviewDTO checkReviewData(Long res_seq);
    List<ReviewDTO> selectReviewList(Long restaurant_seq);
    List<String > selectReviewImages(Long res_seq);
    //    UPDATE
    //    DELETE
}
