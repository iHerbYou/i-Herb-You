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

    // CodeGroup.value
    private static final int REPORT_STATUS_GROUP = 101;        // REPORT_STATUS
    private static final int REVIEW_REPORT_REASON_GROUP = 100; // REVIEW_REPORT_REASON

    // Code.value
    private static final int REPORT_STATUS_PENDING = 1011;     // PENDING

    private final ReviewReportRepository reportRepo;

    @PersistenceContext
    private EntityManager em;

    public ReviewReportService(ReviewReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    /** 리뷰 신고 접수 (중복 방지 + createdAt 세팅) */
    @Transactional
    public ReviewReport createReport(Long userId, Long reviewId, Long reasonCodeIdOrValue) {
        if (reportRepo.existsByReview_IdAndUser_Id(reviewId, userId)) {
            throw new IllegalStateException("이미 신고한 리뷰입니다.");
        }
        if (reasonCodeIdOrValue == null) {
            throw new IllegalArgumentException("신고 사유가 없습니다.");
        }

        Review review = em.find(Review.class, reviewId);
        if (review == null) throw new IllegalArgumentException("리뷰가 없습니다.");
        if (em.find(com.iherbyou.user.entity.User.class, userId) == null) {
            throw new IllegalArgumentException("사용자가 없습니다.");
        }

        // 1) 그룹+value로 시도 (프론트가 value를 보낸 경우)
        Code reason = findCodeByGroupValue(REVIEW_REPORT_REASON_GROUP, reasonCodeIdOrValue.intValue());
        // 2) 실패 시 PK(id)로 시도
        if (reason == null) {
            reason = em.find(Code.class, reasonCodeIdOrValue);
        }
        if (reason == null) throw new IllegalArgumentException("신고 사유를 찾을 수 없습니다.");

        // 초기 상태: REPORT_STATUS.PENDING(1011)
        Code pending = findCodeByGroupValue(REPORT_STATUS_GROUP, REPORT_STATUS_PENDING);
        if (pending == null) throw new IllegalStateException("초기 상태 코드를 찾을 수 없습니다.");

        ReviewReport rr = ReviewReport.builder()
                .user(em.getReference(com.iherbyou.user.entity.User.class, userId))
                .review(review)
                .reasonCode(reason)
                .statusCode(pending)
                .createdAt(LocalDateTime.now())
                .build();

        return reportRepo.save(rr);
    }

    /** 리뷰별 신고 목록 */
    @Transactional(readOnly = true)
    public Page<ReviewReport> listByReview(Long reviewId, Pageable pageable) {
        return reportRepo.findByReview_Id(reviewId, pageable);
    }

    /** 상태 변경(관리자): newStatusValue는 REPORT_STATUS의 code.value */
    @Transactional
    public void changeStatus(Long adminId, boolean isAdmin, Long reportId, Integer newStatusValue) {
        if (!isAdmin) throw new IllegalStateException("권한이 없습니다.");

        reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 건을 찾을 수 없습니다."));

        Code newStatus = findCodeByGroupValue(REPORT_STATUS_GROUP, newStatusValue);
        if (newStatus == null) throw new IllegalArgumentException("상태 코드를 찾을 수 없습니다.");

        int updated = reportRepo.updateStatus(newStatus, LocalDateTime.now(), reportId);
        if (updated == 0) throw new IllegalStateException("상태 변경에 실패했습니다.");
    }

    @Transactional(readOnly = true)
    public long countByReview(Long reviewId) {
        return reportRepo.countByReview_Id(reviewId);
    }

    /** 그룹 value + 코드 value 로 Code 조회 (없으면 null 반환) */
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
                .orElse(null);
    }
}