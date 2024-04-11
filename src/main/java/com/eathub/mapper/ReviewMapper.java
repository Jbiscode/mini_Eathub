package com.eathub.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper {
    //    INSERT
    //   SELECT
    /**
     * 해당 예약의 리뷰가 존재하는지 확인
     * @param res_seq
     */
    int checkReviewData(Long res_seq);
    //    UPDATE
    //    DELETE
}
