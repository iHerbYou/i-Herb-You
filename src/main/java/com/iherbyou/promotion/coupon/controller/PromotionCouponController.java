package com.iherbyou.promotion.coupon.controller;

import com.iherbyou.promotion.coupon.dto.CouponLockResultDto;
import com.iherbyou.promotion.coupon.dto.LockCouponRequest;
import com.iherbyou.promotion.coupon.dto.RedeemCouponRequest;
import com.iherbyou.promotion.coupon.dto.ReleaseCouponRequest;
import com.iherbyou.promotion.coupon.dto.UsableCouponDto;
import com.iherbyou.promotion.coupon.dto.WelcomeCouponResponse;
import com.iherbyou.promotion.coupon.service.PromotionCouponFacade;
import com.iherbyou.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromotionCouponController {

    private final PromotionCouponFacade promotionCouponFacade;

    @PostMapping("/users/{userId}/coupons/welcome")
    public WelcomeCouponResponse issueWelcomeCoupon(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long userId) {
        Long principalId = requirePrincipal(principal);
        ensureSameUser(principalId, userId);
        return promotionCouponFacade.issueWelcomeCoupon(principalId)
                .map(UsableCouponDto::from)
                .map(WelcomeCouponResponse::issuedResponse)
                .orElseGet(WelcomeCouponResponse::skippedResponse);
    }

    @GetMapping("/users/{userId}/coupons/usable")
    public List<UsableCouponDto> getUsableCoupons(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long userId) {
        Long principalId = requirePrincipal(principal);
        ensureSameUser(principalId, userId);
        return promotionCouponFacade.getUsableCoupons(principalId).stream()
                .map(UsableCouponDto::from)
                .toList();
    }

    @PostMapping("/orders/{orderId}/coupons/lock")
    public ResponseEntity<CouponLockResultDto> lockCoupon(@AuthenticationPrincipal UserPrincipal principal,
                                                          @PathVariable Long orderId,
                                                          @Valid @RequestBody LockCouponRequest request) {
        Long principalId = requirePrincipal(principal);
        try {
            return promotionCouponFacade.lockCoupon(principalId, orderId, request.getCouponCode())
                    .map(CouponLockResultDto::from)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no usable coupon found"));
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/orders/{orderId}/coupons/redeem")
    public ResponseEntity<Void> redeemCoupon(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Long orderId,
                                             @Valid @RequestBody RedeemCouponRequest request) {
        Long principalId = requirePrincipal(principal);
        promotionCouponFacade.redeemCoupon(principalId, orderId, request.getUserCouponId(), request.getDiscountAmount());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders/{orderId}/coupons/release")
    public ResponseEntity<Void> releaseCoupon(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long orderId,
                                              @Valid @RequestBody ReleaseCouponRequest request) {
        Long principalId = requirePrincipal(principal);
        promotionCouponFacade.releaseCoupon(principalId, orderId, request.getUserCouponId());
        return ResponseEntity.noContent().build();
    }

    private Long requirePrincipal(UserPrincipal principal) {
        if (principal == null || principal.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authentication required");
        }
        return principal.getId();
    }

    private void ensureSameUser(Long principalId, Long userId) {
        if (!principalId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "userId mismatch");
        }
    }
}
