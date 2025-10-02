package com.iherbyou.community.controller;

import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.community.entity.ReviewReport;
import com.iherbyou.community.service.ReviewReportService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/review-reports")
@RequiredArgsConstructor
public class AdminReviewReportController {

    private final ReviewReportService reportService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    // 특정 리뷰의 신고 목록 (관리자)
    @GetMapping
    public Page<ReviewReport> list(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestParam Long reviewId,
            Pageable pageable
    ) {
        ensureAdmin(me);
        return reportService.listByReview(reviewId, pageable);
    }

    // 신고 상태 변경 (관리자)
    @PatchMapping("/{reportId}/status")
    public void changeStatus(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long reportId,
            @RequestBody ChangeStatusRequest req
    ) {
        ensureAdmin(me);
        // 서비스 시그니처: (adminId, isAdmin, reportId, newStatusCodeId)
        reportService.changeStatus(me.getId(), true, reportId, req.newStatusCodeId());
    }

    public record ChangeStatusRequest(Integer newStatusCodeId) {}

    private void ensureAdmin(UserPrincipal me) {
        if (me != null && me.getAuthorities() != null) {
            boolean hasAdmin = me.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> "ROLE_ADMIN".equals(a) || a.endsWith(":ADMIN") || a.contains("ADMIN"));
            if (hasAdmin) return;
        }

        Integer roleValue = em.createQuery(
                        "select rc.value from User u left join u.roleCode rc where u.id = :uid",
                        Integer.class
                )
                .setParameter("uid", me.getId())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (roleValue == null || !codeService.isAdminRole(roleValue)) {
            throw new AccessDeniedException("FORBIDDEN");
        }
    }
}
