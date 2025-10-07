package com.iherbyou.community.service;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.community.entity.Review;
import com.iherbyou.community.repository.ReviewRepository;
import com.iherbyou.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private static final int MAX_TEXT_LEN = 1000;
    private static final String ORDER_STATUS_DELIVERED = "DELIVERED"; // TODO 실제 코드 사용
    private static final boolean VERIFY_PURCHASE_ENABLED =
            Boolean.parseBoolean(System.getProperty("REVIEWS_VERIFY_PURCHASE", "false"));

    private final ReviewRepository reviewRepo;

    @PersistenceContext
    private EntityManager em;

    public ReviewService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    // ---------- 신규: page/size/sort 오버로드 ----------
    @Transactional(readOnly = true)
    public Page<Review> listByProduct(Long productId, int page, int size, String sort) {
        Pageable pageable = buildPageable(page, size, sort, List.of("createdAt", "id", "rating"));
        return listByProduct(productId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> listMyReviews(Long userId, int page, int size, String sort) {
        Pageable pageable = buildPageable(page, size, sort, List.of("createdAt", "id", "rating"));
        return listMyReviews(userId, pageable);
    }

    // ---------- 기존 (내부/호환용) ----------
    @Transactional(readOnly = true)
    public Page<Review> listByProduct(Long productId, Pageable pageable) {
        return reviewRepo.findByProduct_Id(productId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> listMyReviews(Long userId, Pageable pageable) {
        return reviewRepo.findByUser_Id(userId, pageable);
    }

    // 리뷰 등록
    @Transactional
    public Review createReview(Long userId, Long productId, Integer rating, String text) {
        Product product = em.find(Product.class, productId);
        if (product == null) throw new IllegalArgumentException("상품이 없습니다.");

        User user = em.find(User.class, userId);
        if (user == null) throw new IllegalArgumentException("사용자가 없습니다.");

        if (!isVerifiedPurchaser(userId, productId)) {
            throw new IllegalStateException("구매 이력이 없습니다.");
        }

        if (reviewRepo.existsByUser_IdAndProduct_Id(userId, productId)) {
            throw new IllegalStateException("이미 리뷰를 작성하셨습니다.");
        }

        int r = (rating == null) ? 0 : rating;
        if (r < 1 || r > 5) throw new IllegalArgumentException("평점은 1~5 입니다.");

        String body = sanitizeOptionalText(text, MAX_TEXT_LEN);

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(r)
                .text(body)
                .build();

        return reviewRepo.save(review);
    }

    // 리뷰 내용 수정
    @Transactional
    public void updateReviewText(Long userId, Long reviewId, String newText) {
        String body = sanitizeOptionalText(newText, MAX_TEXT_LEN);
        int updated = reviewRepo.updateTextOwned(reviewId, userId, body);
        if (updated == 0) throw new IllegalStateException("권한이 없거나 존재하지 않습니다.");
    }

    // 소프트 삭제
    @Transactional
    public void softDelete(Long userId, Long reviewId) {
        Review r = reviewRepo.findByIdAndUser_Id(reviewId, userId)
                .orElseThrow(() -> new IllegalStateException("권한이 없거나 존재하지 않습니다."));
        r.softDelete();
    }

    // 집계
    @Transactional(readOnly = true)
    public long countByProduct(Long productId) {
        return reviewRepo.countByProduct_Id(productId);
    }

    @Transactional(readOnly = true)
    public Double averageRating(Long productId) {
        Double avg = reviewRepo.findAverageRatingByProductId(productId);
        return (avg == null) ? 0.0 : avg;
    }

    @Transactional(readOnly = true)
    public long countByRating(Long productId, int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("평점은 1~5 입니다.");
        return reviewRepo.countByProduct_IdAndRating(productId, rating);
    }

    // ---------- 공통 유틸 ----------
    private Pageable buildPageable(int page, int size, String sortParam, List<String> allowedProps) {
        int p = Math.max(0, page);
        int s = Math.max(1, size);

        String prop = "createdAt";
        Sort.Direction dir = Sort.Direction.DESC;

        if (sortParam != null && !sortParam.isBlank()) {
            String[] sp = sortParam.split(",", 2);
            String candidate = sp[0];
            if (allowedProps.contains(candidate)) prop = candidate;
            if (sp.length > 1 && "asc".equalsIgnoreCase(sp[1])) dir = Sort.Direction.ASC;
        }

        return PageRequest.of(p, s, Sort.by(dir, prop));
    }

    private static String sanitizeOptionalText(String text, int max) {
        if (text == null) return null;
        String v = text.trim();
        if (v.isEmpty()) return null;
        if (v.length() > max) throw new IllegalArgumentException("본문이 너무 깁니다.");
        return v;
    }

    // 구매자 검증 (feature flag)
    private boolean isVerifiedPurchaser(Long userId, Long productId) {
        if (!VERIFY_PURCHASE_ENABLED) {
            return true; // 일단 항상 허용
        }
        // TODO: 주문 도메인 연동 후 실제 검증 로직으로 교체
        throw new IllegalStateException(
                "구매자 검증 기능이 아직 연결되지 않았습니다. (ORDER_STATUS=" + ORDER_STATUS_DELIVERED + ")"
        );
    }
}
