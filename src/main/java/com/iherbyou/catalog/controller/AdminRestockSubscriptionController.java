package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.RestockSubscriptionResponse;
import com.iherbyou.catalog.service.RestockSubscriptionService;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restock-subscriptions")
@RequiredArgsConstructor
public class AdminRestockSubscriptionController {

    private final RestockSubscriptionService restockSubscriptionService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    // 재입고 알림 목록 조회
    @GetMapping
    public ResponseEntity<List<RestockSubscriptionResponse>> getAllSubscriptions(
            @RequestParam(required = false) Boolean isActive,
            @AuthenticationPrincipal UserPrincipal me
    ) {
        ensureAdmin(me);
        List<RestockSubscriptionResponse> subscriptions =
                restockSubscriptionService.getAllSubscriptions(isActive);
        return ResponseEntity.ok(subscriptions);
    }

    // 재입고 알림 발송
    @PostMapping("/{id}/notify")
    public ResponseEntity<?> notifyRestock(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal me
    ) {
        ensureAdmin(me);
        restockSubscriptionService.notifyRestock(id);
        return ResponseEntity.ok("Restock notification sent successfully.");
    }

    // 관리자 권한 확인
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
