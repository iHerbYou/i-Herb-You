package com.iherbyou.promotion.coupon.service;

import com.iherbyou.ordering.entity.Order;
import com.iherbyou.promotion.coupon.repository.CouponRepository;
import com.iherbyou.promotion.coupon.repository.UserCouponRepository;
import com.iherbyou.promotion.policy.PromotionPolicyProperties;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.entity.UserCoupon;
import com.iherbyou.user.entity.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionCouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final PromotionPolicyProperties policyProperties;

    public Optional<UserCoupon> issueWelcomeCoupon(User user) {
        if (user == null) {
            return Optional.empty();
        }
        if (!policyProperties.getWelcomeCoupon().isEnabled()) {
            return Optional.empty();
        }
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = couponRepository.findByCode(policyProperties.getWelcomeCoupon().getCouponCode())
                .orElse(null);
        if (coupon == null || !coupon.isIssuableNow(now)) {
            return Optional.empty();
        }
        if (exceedsPerUserLimit(user, coupon)) {
            return Optional.empty();
        }
        UserCoupon userCoupon = createUserCoupon(coupon, user, now);
        coupon.increaseIssuedCount();
        return Optional.of(userCouponRepository.save(userCoupon));
    }

    public List<UserCoupon> getUsableCoupons(Long userId) {
        LocalDate today = LocalDate.now();
        return userCouponRepository.findByUserId(userId).stream()
                .filter(coupon -> coupon.isAvailable(today))
                .toList();
    }

    public Optional<UserCoupon> lockCouponForOrder(Long userId, String couponCode, Order order) {
        LocalDate today = LocalDate.now();
        ensureCouponMinimumOrderAmount(order);
        return userCouponRepository.findFirstByUserIdAndCoupon_CodeAndIsUsedFalseAndExpiredAtGreaterThanEqualOrderByExpiredAtAsc(userId, couponCode, today)
                .filter(userCoupon -> userCoupon.isAvailable(today))
                .map(userCoupon -> {
                    userCoupon.assignOrder(order);
                    return userCoupon;
                });
    }

    public void redeemCoupon(Long couponIssueId, Order order, int redeemedAmount) {
        userCouponRepository.findById(couponIssueId)
                .ifPresent(userCoupon -> userCoupon.markUsed(order));
    }

    public void releaseCoupon(Long couponIssueId) {
        userCouponRepository.findById(couponIssueId).ifPresent(UserCoupon::release);
    }

    public void expireCoupons() {
        LocalDate today = LocalDate.now();
        userCouponRepository.findByIsUsedFalseAndExpiredAtBefore(today)
                .forEach(UserCoupon::expire);
    }

    private UserCoupon createUserCoupon(Coupon coupon, User user, LocalDateTime issuedAt) {
        LocalDate expiryDate = determineExpiryDate(coupon, issuedAt);
        if (expiryDate.isBefore(issuedAt.toLocalDate())) {
            expiryDate = issuedAt.toLocalDate();
        }
        return UserCoupon.builder()
                .user(user)
                .coupon(coupon)
                .issuedAt(issuedAt)
                .expiredAt(expiryDate)
                .build();
    }

    private LocalDate determineExpiryDate(Coupon coupon, LocalDateTime issuedAt) {
        LocalDateTime policyExpiry = calculatePolicyExpiry(issuedAt);
        LocalDateTime couponExpiry = coupon.getCouponEndsAt();
        LocalDateTime finalExpiry = earliestNonNull(policyExpiry, couponExpiry);
        if (finalExpiry == null) {
            finalExpiry = issuedAt.plusYears(1);
        }
        return finalExpiry.toLocalDate();
    }

    private LocalDateTime calculatePolicyExpiry(LocalDateTime issuedAt) {
        int days = Math.max(policyProperties.getWelcomeCoupon().getValidityDays(), 0);
        return days > 0 ? issuedAt.plusDays(days) : null;
    }

    private LocalDateTime earliestNonNull(LocalDateTime a, LocalDateTime b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.isBefore(b) ? a : b;
    }

    private boolean exceedsPerUserLimit(User user, Coupon coupon) {
        Integer limit = coupon.getPerUserLimit();
        if (limit == null || limit <= 0) {
            return false;
        }
        long issuedCount = userCouponRepository.countByUserIdAndCouponId(user.getId(), coupon.getId());
        return issuedCount >= limit;
    }

    private void ensureCouponMinimumOrderAmount(Order order) {
        int threshold = policyProperties.getCouponMinOrderAmount();
        if (threshold <= 0) {
            return;
        }
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        int amount = effectiveOrderAmount(order);
        if (amount < threshold) {
            throw new IllegalStateException("쿠폰은 " + threshold + "원 이상 주문에서만 사용할 수 있습니다.");
        }
    }

    private int effectiveOrderAmount(Order order) {
        int subtotal = safe(order.getSubtotal());
        int deliveryFee = safe(order.getDeliveryFee());
        int discount = safe(order.getDiscount());
        int total = subtotal + deliveryFee - discount;
        return Math.max(total, 0);
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }
}
