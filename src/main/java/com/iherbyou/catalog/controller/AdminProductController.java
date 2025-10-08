package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.ProductCreateRequest;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.service.ProductService;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody ProductCreateRequest request) {

        ensureAdmin(me);
        Product saved = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
