package com.iherbyou.community.repository;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.community.entity.ReviewReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {

    // 1) 리뷰별 신고 목록 (관리자 화면)
    @EntityGraph(attributePaths = {"review", "user", "reasonCode", "statusCode"})
    Page<ReviewReport> findByReview_Id(Long reviewId, Pageable pageable);

    // 2) 상태코드 필터 (접수/승인/기각 등)
    Page<ReviewReport> findByStatusCode_Id(Long statusCodeId, Pageable pageable);

    // 3) 신고자 기준 목록 (마이페이지)
    Page<ReviewReport> findByUser_Id(Long userId, Pageable pageable);

    // 4) 중복 신고 방지용 존재 체크 (user 별 review 한 번만)
    boolean existsByReview_IdAndUser_Id(Long reviewId, Long userId);

    // 5) 리뷰별 신고 개수 (집계/배지)
    long countByReview_Id(Long reviewId);

    // 6) 단건 + 신고자 확인 (정정/취소 정책 있을 때)
    Optional<ReviewReport> findByIdAndUser_Id(Long reportId, Long userId);

    // 7)운영자가 신고 건을 처리하면서 상태를 바꾸는 용도
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ReviewReport r set r.statusCode = :statusCode, r.reviewedAt = :reviewedAt where r.id = :reportId")
    int updateStatus(Code statusCode, java.time.LocalDateTime reviewedAt, Long reportId);
}
