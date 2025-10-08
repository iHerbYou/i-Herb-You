package com.iherbyou.cart.controller;

import com.iherbyou.cart.dto.*;
import com.iherbyou.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 장바구니 API 컨트롤러
 * 로그인 사용자와 비로그인 게스트 모두 장바구니 기능을 사용할 수 있습니다.
 * 게스트는 X-Guest-Token 헤더를 통해 식별됩니다.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 조회
     *
     * @param userDetails 로그인 사용자 정보 (Optional)
     * @param guestToken  게스트 토큰 (Optional, Header: X-Guest-Token)
     * @return 장바구니 정보 (상품 목록, 주문 요약, 추천 상품)
     */
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal UserDetails userDetails, @RequestHeader(value = "X-Guest-Token", required = false) String guestToken) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        CartResponseDTO response = cartService.getCart(email, guestToken);
        return ResponseEntity.ok(response);
    }

    /**
     * 장바구니에 상품 추가
     * 동일한 상품이 이미 있으면 수량을 증가시키고, 없으면 새로 추가합니다.
     * 게스트의 경우 새로운 토큰이 생성될 수 있으며, 응답에 포함됩니다.
     *
     * @param userDetails 로그인 사용자 정보 (Optional)
     * @param guestToken  게스트 토큰 (Optional, Header: X-Guest-Token)
     * @param request     추가할 상품 정보 (productVariantId, qty)
     * @return 장바구니 메시지와 게스트 토큰
     */
    @PostMapping("/items")
    public ResponseEntity<CartMessageResponseDTO> addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestHeader(value = "X-Guest-Token", required = false) String guestToken, @RequestBody AddToCartRequestDTO request) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        CartMessageResponseDTO response = cartService.addToCart(email, guestToken, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 장바구니 상품 수량 변경
     * 재고를 검증한 후 수량을 업데이트합니다.
     *
     * @param cartProductId 장바구니 상품 ID
     * @param userDetails   로그인 사용자 정보 (Optional)
     * @param guestToken    게스트 토큰 (Optional, Header: X-Guest-Token)
     * @param request       변경할 수량 정보 (qty)
     * @return 204 No Content
     */
    @PatchMapping("/items/{cartProductId}/quantity")
    public ResponseEntity<Void> updateQuantity(@PathVariable Long cartProductId, @AuthenticationPrincipal UserDetails userDetails, @RequestHeader(value = "X-Guest-Token", required = false) String guestToken, @RequestBody UpdateQtyRequestDTO request) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        cartService.updateCartProductQty(cartProductId, email, guestToken, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 상품 삭제 (단일)
     *
     * @param cartProductId 장바구니 상품 ID
     * @param userDetails   로그인 사용자 정보 (Optional)
     * @param guestToken    게스트 토큰 (Optional, Header: X-Guest-Token)
     * @return 204 No Content
     */
    @DeleteMapping("/items/{cartProductId}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable Long cartProductId, @AuthenticationPrincipal UserDetails userDetails, @RequestHeader(value = "X-Guest-Token", required = false) String guestToken) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        cartService.deleteCartProduct(cartProductId, email, guestToken);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 상품 선택 삭제
     * cartProductIds가 비어있으면 장바구니 전체를 삭제합니다.
     * cartProductIds에 ID 목록이 있으면 해당 상품들만 삭제합니다.
     *
     * @param userDetails 로그인 사용자 정보 (Optional)
     * @param guestToken  게스트 토큰 (Optional, Header: X-Guest-Token)
     * @param request     삭제할 상품 ID 목록 (빈 배열 시 전체 삭제)
     * @return 204 No Content
     * <p>
     * Request Body 예시:
     * - 선택 삭제: {"cartProductIds": [1, 3, 5]}
     * - 전체 삭제: {"cartProductIds": []} 또는 {"cartProductIds": null}
     */
    @PostMapping("/items/delete-selected")
    public ResponseEntity<Void> deleteSelectedCartProducts(@AuthenticationPrincipal UserDetails userDetails, @RequestHeader(value = "X-Guest-Token", required = false) String guestToken, @RequestBody DeleteSelectedRequestDTO request) {
        String email = userDetails != null ? userDetails.getUsername() : null;

        // 빈 배열이면 전체 삭제
        if (request.getCartProductIds() == null || request.getCartProductIds().isEmpty()) {
            cartService.deleteAllCartProducts(email, guestToken);
        } else {
            cartService.deleteCartProducts(email, guestToken, request.getCartProductIds());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 게스트 장바구니를 로그인 사용자 장바구니와 병합
     * 로그인 시 호출되며(프론트엔드에서 호출), 비로그인 상태에서 담았던 상품들을 사용자 장바구니로 이동합니다.
     * 동일한 상품이 있으면 수량을 합산하고, 게스트 장바구니는 삭제됩니다.
     *
     * @param userDetails 로그인 사용자 정보 (Required)
     * @param guestToken  게스트 토큰 (Optional, Header: X-Guest-Token)
     * @return 200 OK (성공) / 400 Bad Request (비로그인 상태)
     */
    @PostMapping("/merge")
    public ResponseEntity<Void> mergeGuestCart(@AuthenticationPrincipal UserDetails userDetails, @RequestHeader(value = "X-Guest-Token", required = false) String guestToken) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        String email = userDetails.getUsername();
        cartService.mergeGuestCart(email, guestToken);
        return ResponseEntity.ok().build();
    }
}