package com.iherbyou.community.service;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.community.Review;
import com.iherbyou.community.repository.ReviewRepository;
import com.iherbyou.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private static final int MAX_TEXT_LEN = 1000;
    private static final String STATUS_COMPLETED = "COMPLETED"; // enum이면 프로젝트 enum으로 교체

    private final ReviewRepository reviewRepo;

    @PersistenceContext
    private EntityManager em;

    public ReviewService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    // 리뷰 등록: 기본 검증 + 구매자 확인
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

    // 상품별 목록
    @Transactional(readOnly = true)
    public Page<Review> listByProduct(Long productId, Integer rating, Pageable pageable) {
        if (rating == null) return reviewRepo.findByProduct_Id(productId, pageable);
        return reviewRepo.findByProduct_IdAndRating(productId, rating, pageable);
    }

    // 내가 쓴 리뷰
    @Transactional(readOnly = true)
    public Page<Review> listMyReviews(Long userId, Pageable pageable) {
        return reviewRepo.findByUser_Id(userId, pageable);
    }

    // 리뷰 내용 수정 (본인만)
    @Transactional
    public void updateReviewText(Long userId, Long reviewId, String newText) {
        String body = sanitizeOptionalText(newText, MAX_TEXT_LEN);
        int updated = reviewRepo.updateTextOwned(reviewId, userId, body);
        if (updated == 0) throw new IllegalStateException("권한이 없거나 존재하지 않습니다.");
    }

    // 소프트 삭제 (본인만)
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

    private static String sanitizeOptionalText(String text, int max) {
        if (text == null) return null;
        String v = text.trim();
        if (v.isEmpty()) return null;
        if (v.length() > max) throw new IllegalArgumentException("본문이 너무 깁니다.");
        return v;
    }

    // 구매자 확인: 주문 상태가 COMPLETED인 항목이 있는지 간단히 체크
    private boolean isVerifiedPurchaser(Long userId, Long productId) {
        //TODO
//        Long cnt = em.createQuery(
//                        "select count(oi) from com.iherbyou.ordering.OrderProduct oi " +
//                                "where oi.user.id = :uid and oi.product.id = :pid and oi.status = :st",
//                        Long.class)
//                .setParameter("uid", userId)
//                .setParameter("pid", productId)
//                .setParameter("st", STATUS_COMPLETED)
//                .getSingleResult();
//        return cnt != null && cnt > 0;
        return true;
    }
}

 /* 주요 기능
 * - createReview(userId, productId, rating, text)
 *   : 구매자 확인 후 리뷰 1회만 등록(평점 1~5)
 * - listByProduct(productId, rating, pageable)
 *   : 상품별 리뷰 목록(필요하면 평점으로 필터)
 * - listMyReviews(userId, pageable)
 *   : 내가 쓴 리뷰 목록
 * - updateReviewText(userId, reviewId, newText)
 *   : 본인 리뷰만 내용 수정
 * - softDelete(userId, reviewId)
 *   : 본인 리뷰만 소프트 삭제
 * - countByProduct / averageRating / countByRating
 *   : 간단 집계
 *
 *   < 트랜잭션 >
 * - 쓰기 메서드: @Transactional (기본)
 * - 조회 메서드: @Transactional(readOnly = true)
 */