package com.iherbyou.community.controller;

import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.community.entity.ReviewReport;
import com.iherbyou.community.service.ReviewReportService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin/review-reports")
@RequiredArgsConstructor
public class AdminReviewReportController {

    private final ReviewReportService reportService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    // 관계 정렬은 보통 value가 실용적
    private static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "id", "statusCode.value");

    // 특정 리뷰의 신고 목록 (관리자)
    @GetMapping
    public Page<ReviewReport> list(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestParam Long reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        ensureAdmin(me);
        Pageable pageable = buildPageable(page, size, sort, ALLOWED_SORTS);
        return reportService.listByReview(reviewId, pageable);
    }

    // 신고 상태 변경 (관리자) — value로 통일
    @PatchMapping("/{reportId}/status")
    public void changeStatus(
            @AuthenticationPrincipal UserPrincipal me,
            @PathVariable Long reportId,
            @RequestBody ChangeStatusRequest req
    ) {
        ensureAdmin(me);
        reportService.changeStatus(me.getId(), true, reportId, req.newStatusValue());
    }

    public record ChangeStatusRequest(Integer newStatusValue) {}

    private Pageable buildPageable(int page, int size, String sortParam, Set<String> allowedSorts) {
        int p = Math.max(0, page);
        int s = Math.max(1, size);

        String[] sp = sortParam.split(",", 2);
        String prop = sp[0];
        String dirStr = sp.length > 1 ? sp[1] : "desc";
        Sort.Direction dir = "asc".equalsIgnoreCase(dirStr) ? Sort.Direction.ASC : Sort.Direction.DESC;

        String safeProp = allowedSorts.contains(prop) ? prop : "createdAt";
        return PageRequest.of(p, s, Sort.by(dir, safeProp));
    }

    // 관리자 권한 확인 (null 방어 + DB 폴백)
    private void ensureAdmin(UserPrincipal me) {
        if (me == null) throw new AccessDeniedException("FORBIDDEN");

        if (me.getAuthorities() != null) {
            boolean hasAdmin = me.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> "ROLE_ADMIN".equals(a) || a.endsWith(":ADMIN") || a.contains("ADMIN"));
            if (hasAdmin) return;
        }

        Integer roleValue = em.createQuery(
                        "select rc.value from com.iherbyou.user.entity.User u left join u.roleCode rc where u.id = :uid",
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





