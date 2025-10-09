package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.ProductCreateRequest;
import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.dto.ProductUpdateRequest;
import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.service.ProductService;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CodeService codeService;

    @PersistenceContext
    private EntityManager em;

    // 상품 등록 API
    @PostMapping
    public ResponseEntity<?> createProduct(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody ProductCreateRequest request) {

        ensureAdmin(me);
        Product saved = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 상품 목록 조회 API
//    @GetMapping
//    public ResponseEntity<Page<ProductListDto>> getProductsForAdmin(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "8") int size,
//            @RequestParam(required = false) Boolean excludeSoldOut,
//            @RequestParam(required = false) Integer minPrice,
//            @RequestParam(required = false) Integer maxPrice,
//            @RequestParam(required = false) Long categoryId,
//            @RequestParam(required = false) String keyword
//    ) {
//        // 관리자는 품절 포함 여부 선택 가능
//        Page<ProductListDto> result = productService.getProducts(
//                page
//        )
//    }

    // 상품 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal me,
            @RequestBody ProductUpdateRequest request
    ) {
        ensureAdmin(me);
        productService.updateProduct(id, request);
        return ResponseEntity.ok("Product updated successfully");
    }

    // 상품 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal me
    ) {
        ensureAdmin(me);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();  // 204 No Content
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
