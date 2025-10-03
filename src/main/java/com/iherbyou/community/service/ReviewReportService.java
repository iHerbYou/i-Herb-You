package com.iherbyou.community.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.community.entity.Review;
import com.iherbyou.community.entity.ReviewReport;
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
