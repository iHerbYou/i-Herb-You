package com.iherbyou.community.repository;

import com.iherbyou.community.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1) 상품별 목록(페이지) — N+1 방지용 포함
    @EntityGraph(attributePaths = {"product","user"})
    Page<Review> findByProduct_Id(Long productId, Pageable pageable);

    // 2) 상품 + 별점 필터 목록
    @EntityGraph(attributePaths = {"product","user"})
    Page<Review> findByProduct_IdAndRating(Long productId, Integer rating, Pageable pageable);

    // 3) 사용자-상품 리뷰 존재 여부 (재작성 방지/보조 검증)
    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);

    // 4) 상품별 리뷰 개수
    long countByProduct_Id(Long productId);

    // 5) 작성자 기준 단건(수정/삭제 권한체크)
    Optional<Review> findByIdAndUser_Id(Long reviewId, Long userId);

    // 6) 마이페이지 리뷰 모아보기 (product 정보까지 로딩)
    @EntityGraph(attributePaths = {"product"})
    Page<Review> findByUser_Id(Long userId, Pageable pageable);

    // 7) 소유자 텍스트 업데이트
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Review r set r.text = :text where r.id = :reviewId and r.user.id = :userId")
    int updateTextOwned(Long reviewId, Long userId, String text);

    // 8) 평균 평점
    @Query("select" +
            " avg(r.rating) from Review r where r.product.id = :productId")
    Double findAverageRatingByProductId(Long productId);

    // 9) 평점 분포
    long countByProduct_IdAndRating(Long productId, Integer rating);

}

