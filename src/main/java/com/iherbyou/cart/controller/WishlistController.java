package com.iherbyou.cart.controller;

import com.iherbyou.cart.dto.*;
import com.iherbyou.cart.service.WishlistService;

import com.iherbyou.cart.service.WishlistShareService;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Validated
public class WishlistController {

    private final WishlistService wishlistService;
    private final WishlistShareService wishlistShareService;

    // 1) 위시리스트 조회
    @GetMapping
    public WishlistPageResponse getMyWishlist(@AuthenticationPrincipal UserPrincipal me) {
        final int LIMIT = 20; // wishlist max items
        return wishlistService.getMyWishlist(me.getId(), LIMIT);
    }

    // 2) 아이템 추가
    @PostMapping
    public ResponseEntity<AddWishlistItemResponse> addToWishlist(@AuthenticationPrincipal UserPrincipal me,
                                                                 @RequestParam Long productId) {
        Long userId = me.getId();
        AddWishlistItemResponse res = wishlistService.addItem(userId, productId);
        return res.isDuplicated() ? ResponseEntity.ok(res) : ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 3) 아이템 삭제 (단건/복수)
    @DeleteMapping(value = "/items")
    public DeleteItemsResponse deleteItems(@AuthenticationPrincipal UserPrincipal me, @RequestBody @Valid DeleteItemsRequest req) {
        if (req == null || req.getItemIds() == null || req.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("itemIds는 필수입니다.");
        }
        int deletedCount = 0;
        for (Long itemId : req.getItemIds()) {
            wishlistService.removeItem(me.getId(), itemId);
            deletedCount++;
        }
        return DeleteItemsResponse.builder()
                .deletedCount(deletedCount)
                .message("위시리스트에서 제거되었습니다.")
                .build();
    }

    // 4) 위시리스트 공유 생성 (전체 위시리스트 스냅샷)
    @PostMapping(value="/share")
    public ResponseEntity<CreateWishlistShareResponse> createShare(
            @AuthenticationPrincipal UserPrincipal me,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey // 중복 방지 uuid
    ) {
        CreateWishlistShareResponse res = wishlistShareService.createShare(idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 5) 위시리스트 공유 보기
    @GetMapping(value = "/wishlist/{shareId}", produces = "application/json")
    public SharedWishlistResponse viewWishlistShare(@PathVariable String shareId) {
        return wishlistShareService.viewShare(shareId);
    }
}
