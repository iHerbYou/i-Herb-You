package com.iherbyou.wishlist.controller;

import com.iherbyou.wishlist.dto.request.DeleteItemsRequest;
import com.iherbyou.wishlist.service.WishlistService;

import com.iherbyou.wishlist.service.WishlistShareService;
import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.wishlist.dto.response.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final WishlistShareService wishlistShareService;

    // 1) 위시리스트 조회
    @GetMapping
    public ResponseEntity<WishlistResponse> getMyWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        WishlistResponse response = wishlistService.getMyWishlist(userPrincipal.getId());
        return ResponseEntity.ok(response);
    }

    // 2) 아이템 추가
    @PostMapping
    public ResponseEntity<AddWishlistItemResponse> addToWishlist(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam @Positive(message = "상품 ID는 양수여야 합니다.") Long productId) {

        AddWishlistItemResponse response = wishlistService.addItem(
                userPrincipal.getId(),
                productId
        );

        return ResponseEntity.ok(response);
    }

    // 3) 아이템 삭제 (단건/복수)
    @DeleteMapping(value = "/items")
    public ResponseEntity<Void> deleteItems(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid DeleteItemsRequest req) {

        for (Long itemId : req.getItemIds()) {
            wishlistService.removeItem(userPrincipal.getId(), itemId);
        }

        return ResponseEntity.noContent().build();
    }

    // 4) 위시리스트 공유 생성 (전체 위시리스트 스냅샷)
    @PostMapping(value = "/share")
    public ResponseEntity<CreateWishlistShareResponse> createShare(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CreateWishlistShareResponse res = wishlistShareService.createShare(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 5) 위시리스트 공유 보기
    @GetMapping(value = "/share/{shareId}")
    public SharedWishlistResponse viewWishlistShare(@PathVariable String shareId) {
        return wishlistShareService.viewShare(shareId);
    }
}
