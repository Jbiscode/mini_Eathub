package com.eathub.service;

import com.eathub.conf.SessionConf;
import com.eathub.dto.ReviewDTO;
import com.eathub.mapper.RestaurantMapper;
import com.eathub.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
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
//        ncpObjectStorageService.uploadFile(reviewDTO);
        ReviewDTO reviewDTOs = uploadFile(reviewDTO, session);
        reviewMapper.insertReview(reviewDTOs);
        reviewMapper.insertReviewImages(reviewDTOs);
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

    public ReviewDTO isReviewed(Long resSeq) {
        return reviewMapper.selectReviewed(resSeq);
    }
    //    UPDATE
    //    DELETE
}
