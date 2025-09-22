package com.iherbyou.community.service;

import com.iherbyou.user.entity.User;
import com.iherbyou.common.code.entity.Code;
import com.iherbyou.community.Review;
import com.iherbyou.community.ReviewReport;
import com.iherbyou.community.repository.ReviewReportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewReportService {

    private static final Long STATUS_RECEIVED = 1001L;
    private final ReviewReportRepository reportRepo;

    @PersistenceContext
    private EntityManager em;

    public ReviewReportService(ReviewReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    // 리뷰 신고접수(중복방지)
    @Transactional
    public ReviewReport createReport(Long userId, Long reviewId, Long reasonCodeId) {
        if (reportRepo.existsByReview_IdAndUser_Id(reviewId, userId)) {
            throw new IllegalStateException("이미 신고한 리뷰입니다.");
        }

        Review review = em.find(Review.class, reviewId);
        if (review == null) throw new IllegalArgumentException("리뷰가 없습니다.");
        if (em.find(com.iherbyou.user.entity.User.class, userId) == null) {
            throw new IllegalArgumentException("사용자가 없습니다.");
        }

        Code reason = em.find(Code.class, reasonCodeId);
        if (reason == null) throw new IllegalArgumentException("신고 사유를 찾을 수 없습니다.");

        Code received = em.find(Code.class, STATUS_RECEIVED);
        if (received == null) throw new IllegalArgumentException("상태 코드를 찾을 수 없습니다.");

        ReviewReport rr = ReviewReport.builder()
                .user(em.getReference(com.iherbyou.user.entity.User.class, userId))
                .review(review)
                .reasonCode(reason)
                .statusCode(received)
                .build();

        return reportRepo.save(rr);
    }

    // 목록
    @Transactional(readOnly = true)
    public Page<ReviewReport> listByReview(Long reviewId, Pageable pageable) {
        return reportRepo.findByReview_Id(reviewId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReviewReport> listByReporter(Long userId, Pageable pageable) {
        return reportRepo.findByUser_Id(userId, pageable);
    }

    // 상태변경(관리자)
    @Transactional
    public void changeStatus(Long adminId, boolean isAdmin, Long reportId, Integer newStatusCodeId) {
        if (!isAdmin) throw new IllegalStateException("권한이 없습니다.");

        reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 건을 찾을 수 없습니다."));

        Code newStatus = em.find(Code.class, newStatusCodeId);
        if (newStatus == null) throw new IllegalArgumentException("상태 코드를 찾을 수 없습니다.");

        int updated = reportRepo.updateStatus(newStatus, LocalDateTime.now(), reportId);
        if (updated == 0) throw new IllegalStateException("상태 변경에 실패했습니다.");
    }

    @Transactional(readOnly = true)
    public long countByReview(Long reviewId) {
        return reportRepo.countByReview_Id(reviewId);
    }
}

 /* 주요 기능
 * - createReport(userId, reviewId, reasonCodeId)
 *   : 중복 신고 방지 후 신고 접수(사유/초기 상태 설정)
 * - listByReview(reviewId, pageable)
 *   : 특정 리뷰에 대한 신고 목록
 * - listByReporter(userId, pageable)
 *   : 내가 제출한 신고 목록
 * - changeStatus(adminId, isAdmin, reportId, newStatusCodeId)
 *   : 관리자 권한으로 신고 상태 변경
 * - countByReview(reviewId)
 *   : 리뷰별 신고 건수
 *
         * 트랜잭션
 * - 쓰기/상태변경: @Transactional
 * - 조회: @Transactional(readOnly = true)
 */
