package com.eathub.api;

import com.eathub.dto.ReviewDTO;
import com.eathub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewApi {
    private final ReviewService reviewService;
    @GetMapping("")
    public ResponseEntity<?> getReviews(@RequestParam(value = "page",defaultValue = "1") int page, @RequestParam("restaurant_seq") Long restaurant_seq) {
        List<ReviewDTO> reviewDTOs = reviewService.selectReviewAndImages(restaurant_seq, page);
        if (reviewDTOs != null) {
            return ResponseEntity.ok(reviewDTOs);
        }
        return ResponseEntity.notFound().build();
    }
}
