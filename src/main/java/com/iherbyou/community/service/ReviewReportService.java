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

    // ===== Code 시드 기준 상수 (group.value / code.value) =====
    // 그룹: REPORT_STATUS (101)
    private static final int REPORT_STATUS_GROUP = 101;
    // 상태값: PENDING(접수) = 1011, REVIEWED = 1012, ACTION_TAKEN = 1013, REJECTED = 1014 ...
    private static final int REPORT_STATUS_PENDING = 1011;

    private final ReviewReportRepository reportRepo;

    @PersistenceContext
    private EntityManager em;

    public ReviewReportService(ReviewReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    // 리뷰 신고 접수 (중복 방지)
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

        // 사유 코드는 프론트에서 Code PK(id)를 전달한다고 가정 (변경 없으면 그대로 사용)
        Code reason = em.find(Code.class, reasonCodeId);
        if (reason == null) throw new IllegalArgumentException("신고 사유를 찾을 수 없습니다.");

        // 초기 상태: REPORT_STATUS.PENDING(1011) — 그룹/값으로 안전 조회
        Code pending = findCodeByGroupValue(REPORT_STATUS_GROUP, REPORT_STATUS_PENDING);

        ReviewReport rr = ReviewReport.builder()
                .user(em.getReference(com.iherbyou.user.entity.User.class, userId))
                .review(review)
                .reasonCode(reason)
                .statusCode(pending)
                .build();

        return reportRepo.save(rr);
    }

    // 목록
    @Transactional(readOnly = true)
    public Page<ReviewReport> listByReview(Long reviewId, Pageable pageable) {
        return reportRepo.findByReview_Id(reviewId, pageable);
    }

    // 상태 변경 (관리자)
    @Transactional
    public void changeStatus(Long adminId, boolean isAdmin, Long reportId, Integer newStatusValue) {
        if (!isAdmin) throw new IllegalStateException("권한이 없습니다.");

        reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 건을 찾을 수 없습니다."));

        // 전달값을 code.value 로 간주하고 조회 (PK가 아닌 value 기반)
        Code newStatus = findCodeByGroupValue(REPORT_STATUS_GROUP, newStatusValue);

        int updated = reportRepo.updateStatus(newStatus, LocalDateTime.now(), reportId);
        if (updated == 0) throw new IllegalStateException("상태 변경에 실패했습니다.");
    }

    @Transactional(readOnly = true)
    public long countByReview(Long reviewId) {
        return reportRepo.countByReview_Id(reviewId);
    }

    // ===== 내부 유틸: 그룹 value + 코드 value 로 Code 엔티티 안전 조회 =====
    private Code findCodeByGroupValue(int groupValue, int codeValue) {
        return em.createQuery("""
                select c
                  from Code c
                  join c.codeGroup g
                 where g.value = :g and c.value = :v
                """, Code.class)
                .setParameter("g", groupValue)
                .setParameter("v", codeValue)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "코드를 찾을 수 없습니다. group=" + groupValue + ", value=" + codeValue));
    }
}
