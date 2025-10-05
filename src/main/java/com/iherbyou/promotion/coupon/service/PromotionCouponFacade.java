package com.iherbyou.promotion.coupon.service;

import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.promotion.coupon.repository.UserCouponRepository;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.entity.UserCoupon;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionCouponFacade {

    private final PromotionCouponService promotionCouponService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserCouponRepository userCouponRepository;

    public Optional<UserCoupon> issueWelcomeCoupon(Long userId) {
        User user = requireUser(userId);
        return promotionCouponService.issueWelcomeCoupon(user);
    }

    public List<UserCoupon> getUsableCoupons(Long userId) {
        requireUser(userId);
        return promotionCouponService.getUsableCoupons(userId);
    }

    public Optional<UserCoupon> lockCoupon(Long orderId, String couponCode) {
        Order order = requireOrder(orderId);
        User user = requireOrderUser(order);
        return promotionCouponService.lockCouponForOrder(user.getId(), couponCode, order);
    }

    public void redeemCoupon(Long orderId, Long userCouponId, int discountAmount) {
        Order order = requireOrder(orderId);
        UserCoupon coupon = requireUserCoupon(userCouponId);
        ensureCouponLockedToOrder(coupon, orderId);
        promotionCouponService.redeemCoupon(userCouponId, order, discountAmount);
    }

    public void releaseCoupon(Long orderId, Long userCouponId) {
        requireOrder(orderId);
        UserCoupon coupon = requireUserCoupon(userCouponId);
        ensureCouponBelongsOrUnassigned(coupon, orderId);
        promotionCouponService.releaseCoupon(userCouponId);
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    private Order requireOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
    }

    private User requireOrderUser(Order order) {
        if (order.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order user missing");
        }
        return order.getUser();
    }

    private UserCoupon requireUserCoupon(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user coupon not found"));
    }

    private void ensureCouponLockedToOrder(UserCoupon coupon, Long orderId) {
        if (coupon.getOrder() == null || coupon.getOrder().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon is not locked to an order");
        }
        if (!coupon.getOrder().getId().equals(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon does not belong to order");
        }
    }

    private void ensureCouponBelongsOrUnassigned(UserCoupon coupon, Long orderId) {
        if (coupon.getOrder() != null && coupon.getOrder().getId() != null
                && !coupon.getOrder().getId().equals(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon does not belong to order");
        }
    }
}
