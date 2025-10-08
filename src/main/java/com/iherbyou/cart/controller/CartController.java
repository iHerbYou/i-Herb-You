package com.iherbyou.cart.controller;

import com.iherbyou.cart.dto.CartDTO.*;
import com.iherbyou.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader(value = "X-Guest-Token", required = false) String guestToken
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        CartResponseDTO response = cartService.getCart(email, guestToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<CartMessageResponseDTO> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader(value = "X-Guest-Token", required = false) String guestToken,
            @RequestBody AddToCartRequestDTO request
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        CartMessageResponseDTO response = cartService.addToCart(email, guestToken, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/items/{cartProductId}/quantity")
    public ResponseEntity<Void> updateQuantity(
            @PathVariable Long cartProductId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader(value = "X-Guest-Token", required = false) String guestToken,
            @RequestBody UpdateQtyRequestDTO request
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        cartService.updateCartProductQty(cartProductId, email, guestToken, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{cartProductId}")
    public ResponseEntity<Void> deleteCartProduct(
            @PathVariable Long cartProductId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader(value = "X-Guest-Token", required = false) String guestToken
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        cartService.deleteCartProduct(cartProductId, email, guestToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/items/delete-selected")  // ✅ POST로 변경
    public ResponseEntity<Void> deleteSelectedCartProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader(value = "X-Guest-Token", required = false) String guestToken,
            @RequestBody DeleteSelectedRequestDTO request  // ✅ request 사용
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;

        // 빈 배열이면 전체 삭제
        if (request.getCartProductIds() == null || request.getCartProductIds().isEmpty()) {
            cartService.deleteAllCartProducts(email, guestToken);
        } else {
            cartService.deleteCartProducts(email, guestToken, request.getCartProductIds());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/merge")
    public ResponseEntity<Void> mergeGuestCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader(value = "X-Guest-Token", required = false) String guestToken
    ) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        String email = userDetails.getUsername();
        cartService.mergeGuestCart(email, guestToken);
        return ResponseEntity.ok().build();
    }
}