package com.iherbyou.community.controller;

import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.community.entity.Review;
import com.iherbyou.community.service.ReviewService;
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
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    // 상품 기준 리뷰 목록 (관리자)
    @GetMapping
    public Page<Review> listByProduct(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestParam Long productId,
            Pageable pageable
    ) {
        ensureAdmin(me);
        return reviewService.listByProduct(productId, pageable);
    }

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
