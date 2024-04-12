package com.eathub.mapper;

import com.eathub.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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


    ReviewDTO selectReviewed(Long resSeq);

    // 리뷰 전체 조회
    List<ReviewDTO> selectReviewList(Long restaurant_seq);
    // 리뷰 일부 조회
    List<ReviewDTO> selectReviewListPage(@Param("restaurant_seq") Long restaurant_seq,@Param("page") int page);
    List<String > selectReviewImages(Long res_seq);

    //    UPDATE
    //    DELETE
}
