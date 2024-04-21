package com.eathub.service;

import com.eathub.conf.SessionConf;

import com.eathub.dto.LoginDTO;

import com.eathub.dto.ReviewDTO;
import com.eathub.dto.ReviewStatsDTO;
import com.eathub.mapper.RestaurantMapper;
import com.eathub.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final RestaurantMapper restaurantMapper;
    private final ReviewMapper reviewMapper;
    private final NCPObjectStorageService ncpObjectStorageService;

    //    INSERT
    /**
     * 리뷰 등록
     *
     * @param reviewDTO
     */
    @Transactional
    public void insertReviewAndImages(ReviewDTO reviewDTO, HttpSession session) {

        ReviewDTO reviewDTOs = uploadFile(reviewDTO, session);
        reviewMapper.insertReview(reviewDTOs);
        reviewMapper.insertReviewImages(reviewDTOs);
        ReviewStatsDTO reviewStatsDTO=reviewMapper.calculateReviewStats(reviewDTO.getRestaurant_seq());
        reviewMapper.updateReviewCountAndRatingAvg(reviewDTO.getRestaurant_seq(),reviewStatsDTO.getReview_count(),reviewStatsDTO.getRating_avg());
    }
    private ReviewDTO uploadFile(ReviewDTO reviewDTO, HttpSession session) {
        String BucketFolderName = "review/";
        String UUID;
        String imageOriginalName;
        File file;

        String filepath = session.getServletContext().getRealPath("WEB-INF/storage");
        System.out.println("실제폴더 = " + filepath);

        List<CommonsMultipartFile> reviewImages = reviewDTO.getReviewImages();
        List<String> pictureUrls = new ArrayList<>();

        for (CommonsMultipartFile img : reviewImages) {
            // 이미지 파일 저장
            if (img != null) {
                imageOriginalName = img.getOriginalFilename();
                // NCP Object Storage에 이미지 업로드
                UUID = ncpObjectStorageService.uploadFile(SessionConf.BUCKET_NAME, BucketFolderName, img);
                file = new File(filepath, imageOriginalName);
                try {
                    img.transferTo(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                pictureUrls.add(UUID);
            }
        }
        reviewDTO.setPictureUrls(pictureUrls);

        return reviewDTO;
    }
    //    SELECT

    /**
     * 해당 예약의 리뷰가 존재하는지 확인
     *
     * @param res_seq
     */
    public String checkReviewData(Long res_seq, Long member_seq) {
        ReviewDTO dto = reviewMapper.checkReviewData(res_seq);
        if (dto != null) {
            if (!dto.getMember_seq().equals(member_seq) || !(dto.getExisting_review() ==null)) {
                return "접근권한";
            }
        }
        return "access granted";
    }

/* 리뷰페이지에서 리뷰당 갯수 구하기*/
    public List<ReviewStatsDTO> selectReviewCount(Long restaurant_seq){
        return reviewMapper.selectReviewCount(restaurant_seq);
    }
    
    // 리뷰 리스트 전체 조회
    public List<ReviewDTO> selectReviewAndImages(Long res_seq) {
        List<ReviewDTO> reviewDTO = reviewMapper.selectReviewList(res_seq);
        for (ReviewDTO dto : reviewDTO) {
            List<String> reviewImages = reviewMapper.selectReviewImages(dto.getRes_seq());
            dto.setPictureUrls(reviewImages);
        }
        return reviewDTO;
    }

    @Transactional
    // 리뷰 리스트 페이지 조회
    public List<ReviewDTO> selectReviewAndImages(Long restaurant_seq, int page) {
        List<ReviewDTO> reviewDTO = reviewMapper.selectReviewListPage(restaurant_seq, (page-1)*2);
        for (ReviewDTO dto : reviewDTO) {
            List<String> reviewImages = reviewMapper.selectReviewImages(dto.getRes_seq());
            dto.setPictureUrls(reviewImages);
        }
        return reviewDTO;
    }

    //고객 리뷰관리 페이지
    public List<ReviewDTO> selectMyReviewAndImages(Long member_seq, int page) {
        List<ReviewDTO> reviewDTO = reviewMapper.selectMyReviewListPage(member_seq, (page-1)*2);
        for (ReviewDTO dto : reviewDTO) {
            List<String> reviewImages = reviewMapper.selectReviewImages(dto.getRes_seq());
            dto.setPictureUrls(reviewImages);
        }
        return reviewDTO;
    }

    //점주 리뷰관리 페이지
    public List<ReviewDTO> selectOwnerReviewAndImages(Long member_seq, int page) {
        List<ReviewDTO> reviewDTO = reviewMapper.selectOwnerReviewListPage(member_seq, (page-1)*2);
        for (ReviewDTO dto : reviewDTO) {
            List<String> reviewImages = reviewMapper.selectReviewImages(dto.getRes_seq());
            dto.setPictureUrls(reviewImages);
        }

        return reviewDTO;
    }

    //고객 총 리뷰수
    public Long totalReviewCount(Long member_seq) {
        Long total_review = reviewMapper.totalReviewCount(member_seq);

        return total_review;
    }

    //점주 총 리뷰수
    public Long ownerTotalReviewCount(Long member_seq) {
        Long total_review = reviewMapper.ownerTotalReviewCount(member_seq);

        return total_review;
    }
    //    UPDATE
    //    DELETE
}
