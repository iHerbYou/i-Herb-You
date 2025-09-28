package com.iherbyou.cart.controller;

import com.iherbyou.cart.dto.AddWishlistItemResponse;
import com.iherbyou.cart.dto.DeleteItemsResponse;
import com.iherbyou.cart.dto.DeleteItemsRequest;
import com.iherbyou.cart.dto.WishlistPageResponse;
import com.iherbyou.cart.service.WishlistService;

import jakarta.validation.Valid;
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
    // 3) 아이템 삭제 (단건/복수) - Request Body 사용
    //    - DELETE /api/wishlist/items
    //    - Body 예시:
    //      {
    //        "userId": 1,
    //        "itemIds": [10, 11]
    //      }
    // ---------------------------
    @DeleteMapping(value = "/items")
    public DeleteItemsResponse deleteItems(@RequestBody @Valid DeleteItemsRequest req) {
        if (req == null || req.getUserId() == null || req.getItemIds() == null || req.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("userId와 itemIds는 필수입니다.");
        }
        int deletedCount = 0;
        for (Long itemId : req.getItemIds()) {
            wishlistService.removeItem(req.getUserId(), itemId);
            deletedCount++;
        }
        return DeleteItemsResponse.builder()
                .deletedCount(deletedCount)
                .message("위시리스트에서 제거되었습니다.")
                .build();
    }

}