package com.iherbyou.community.controller;

import com.iherbyou.community.dto.ReviewCreateRequest;
import com.iherbyou.community.dto.ReviewProduct;
import com.iherbyou.community.dto.ReviewSummary;
import com.iherbyou.community.entity.Review;
import com.iherbyou.community.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Swagger/프론트 실수 방지: 허용 정렬 필드 화이트리스트
    private static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "id", "rating");

    /**
     * 리뷰 등록
     * Header: X-USER-ID
     * Body: ReviewCreateRequest { productId, rating, text }
     * Response: ReviewProduct
     */
    @PostMapping
    public ResponseEntity<ReviewProduct> createReview(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody ReviewCreateRequest req
    ) {
        Review saved = reviewService.createReview(userId, req.productId(), req.rating(), req.text());
        ReviewProduct res = new ReviewProduct(
                saved.getId(),
                saved.getRating(),
                saved.getText(),
                saved.getUser().getName(),
                saved.getCreatedAt().format(ISO)
        );
        return ResponseEntity.ok(res);
    }

    /**
     * 상품별 리뷰 목록
     * Query: productId
     * Pageable: 기본 createdAt desc (잘못된 sort가 오면 기본값으로 대체)
     */
    @GetMapping
    public ResponseEntity<Page<ReviewProduct>> listByProduct(
            @RequestParam Long productId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        pageable = safePageable(pageable);
        Page<Review> page = reviewService.listByProduct(productId, pageable);
        Page<ReviewProduct> body = page.map(r -> new ReviewProduct(
                r.getId(),
                r.getRating(),
                r.getText(),
                r.getUser().getName(),
                r.getCreatedAt().format(ISO)
        ));
        return ResponseEntity.ok(body);
    }

    /**
     * 리뷰 통계 요약 (총개수/평균/별점분포)
     * Query: productId
     * Response: ReviewSummary { total, average, counts[1~5] }
     */
    @GetMapping("/summary")
    public ResponseEntity<ReviewSummary> summary(@RequestParam Long productId) {
        long total = reviewService.countByProduct(productId);
        double avg = reviewService.averageRating(productId);
        long[] counts = new long[5];
        for (int i = 1; i <= 5; i++) {
            counts[i - 1] = reviewService.countByRating(productId, i);
        }
        return ResponseEntity.ok(new ReviewSummary(total, avg, counts));
    }

    // ===== 내부 유틸: 허용된 정렬 필드만 반영해 안전한 Pageable로 변환 =====
    private Pageable safePageable(Pageable pageable) {
        List<Sort.Order> filtered = pageable.getSort().stream()
                .filter(o -> ALLOWED_SORTS.contains(o.getProperty()))
                .toList();

        Sort sort = filtered.isEmpty()
                ? Sort.by(Sort.Direction.DESC, "createdAt")
                : Sort.by(filtered);

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
