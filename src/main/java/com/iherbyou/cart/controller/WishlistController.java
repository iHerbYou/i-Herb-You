package com.iherbyou.cart.controller;

import com.iherbyou.cart.dto.AddWishlistItemRequest;
import com.iherbyou.cart.dto.AddWishlistItemResponse;
import com.iherbyou.cart.dto.BulkDeleteResponse;
import com.iherbyou.cart.dto.BulkDeleteWishlistItemsRequest;
import com.iherbyou.cart.dto.WishlistPageResponse;
import com.iherbyou.cart.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// 보안 붙은 후에 사용될 사용자 정보 객체 (프로젝트에 맞게 변경)
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import com.iherbyou.security.AuthUser; // 실제 AuthUser 타입을 사용한다면 주석 해제

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Validated
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * 인증 적용 전/후 겸용 유저 식별자 해석기
     * - 보안 붙은 후: @AuthenticationPrincipal me 가 우선
     * - 지금: ?userId= 로 대체 사용 (테스트/개발용)
     */
    private Long resolveUserId(Long userIdParam) {
        if (userIdParam == null) {
            throw new UnauthorizedException("로그인이 필요합니다. (개발 단계에서는 ?userId= 를 전달하세요)");
        }
        return userIdParam;
    }

    // ---------------------------
    // 1) 위시리스트 조회 (커서 페이징)
    // ---------------------------
    @GetMapping
    public WishlistPageResponse list(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) Long userId // 개발/테스트용
    ) {
        Long userIdResolved = resolveUserId(userId);
        int pageSize = Math.min(Math.max(limit, 1), 50); // 1~50 방어
        return wishlistService.getMyWishlist(userIdResolved, cursor, pageSize);
    }

    // ---------------------------
    // 2) 아이템 추가
    // ---------------------------
//    @PostMapping("/items")
//    public ResponseEntity<AddWishlistItemResponse> add(
//            @RequestBody @Valid AddWishlistItemRequest req,
//            @RequestParam(required = false) Long userId // 개발/테스트용
//    ) {
//        Long userIdResolved = resolveUserId(userId);
//        AddWishlistItemResponse res = wishlistService.addItem(userIdResolved, req.getProductId());
//        return res.isDuplicated()
//                ? ResponseEntity.ok(res)
//                : ResponseEntity.status(HttpStatus.CREATED).body(res);
//    }

    // ---------------------------
    // 3) 아이템 단건 삭제
    // ---------------------------
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long itemId,
            @RequestParam(required = false) Long userId // 개발/테스트용
    ) {
        Long userIdResolved = resolveUserId(userId);
        wishlistService.removeItem(userIdResolved, itemId);
    }

    // ---------------------------
    // 4) 아이템 벌크 삭제 (선택)
    // ---------------------------
    @DeleteMapping("/items:bulk")
    public BulkDeleteResponse bulkDelete(
            @RequestBody @Valid BulkDeleteWishlistItemsRequest req,
            @RequestParam(required = false) Long userId // 개발/테스트용
    ) {
        Long userIdResolved = resolveUserId(userId);
        int count = wishlistService.bulkRemove(userIdResolved, req.getItemIds());
        return BulkDeleteResponse.builder().deletedCount(count).build();
    }

    // ---------------------------
    // 공통 예외 (간단 샘플)
    // ---------------------------
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}