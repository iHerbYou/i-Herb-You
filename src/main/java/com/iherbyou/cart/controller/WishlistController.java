package com.iherbyou.cart.controller;

import com.iherbyou.cart.dto.AddWishlistItemResponse;
import com.iherbyou.cart.dto.BulkDeleteResponse;
import com.iherbyou.cart.dto.WishlistPageResponse;
import com.iherbyou.cart.service.WishlistService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// 보안 붙은 후에 사용될 사용자 정보 객체 (프로젝트에 맞게 변경)
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import com.iherbyou.security.AuthUser; // 실제 AuthUser 타입을 사용한다면 주석 해제

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Validated
public class WishlistController {

    private final WishlistService wishlistService;

    // ---------------------------
    // 1) 위시리스트 조회 (커서 페이징)
    // ---------------------------
    @GetMapping(params = "userId")
    public WishlistPageResponse list(@RequestParam Long userId) {
        final int LIMIT = 20; // wishlist max items
        return wishlistService.getMyWishlist(userId, LIMIT);
    }

    // ---------------------------
    // 2) 아이템 추가
    // ---------------------------
    @PostMapping(params = "userId")
    public ResponseEntity<AddWishlistItemResponse> add(@RequestParam Long userId, @RequestParam Long productId) {
        AddWishlistItemResponse res = wishlistService.addItem(userId, productId);
        return res.isDuplicated() ? ResponseEntity.ok(res) : ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // ---------------------------
    // 3) 아이템 단건 삭제
    // ---------------------------
    @DeleteMapping(value = "/{itemId}", params = "userId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long itemId, @RequestParam Long userId) {
        wishlistService.removeItem(userId, itemId);
    }

    // ---------------------------
    // 4) 아이템 전체 삭제
    // ---------------------------
    @DeleteMapping(value = "/items", params = "userId")
    public BulkDeleteResponse deleteAll(@RequestParam Long userId) {
        int count = wishlistService.bulkRemoveAll(userId);
        return BulkDeleteResponse.builder().deletedCount(count).build();
    }

}