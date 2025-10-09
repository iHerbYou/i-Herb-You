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

    public Optional<UserCoupon> lockCoupon(Long userId, Long orderId, String couponCode) {
        Order order = requireOrder(orderId);
        User user = requireOrderUser(order);
        ensureUserMatches(user, userId);
        return promotionCouponService.lockCouponForOrder(userId, couponCode, order);
    }

    public void redeemCoupon(Long userId, Long orderId, Long userCouponId, int discountAmount) {
        Order order = requireOrder(orderId);
        ensureUserMatches(requireOrderUser(order), userId);
        UserCoupon coupon = requireUserCoupon(userCouponId);
        ensureCouponLockedToOrder(coupon, orderId);
        ensureCouponOwner(coupon, userId);
        promotionCouponService.redeemCoupon(userCouponId, order, discountAmount);
    }

    public void releaseCoupon(Long userId, Long orderId, Long userCouponId) {
        Order order = requireOrder(orderId);
        ensureUserMatches(requireOrderUser(order), userId);
        UserCoupon coupon = requireUserCoupon(userCouponId);
        ensureCouponBelongsOrUnassigned(coupon, orderId);
        ensureCouponOwner(coupon, userId);
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

    private void ensureUserMatches(User user, Long userId) {
        if (user == null || user.getId() == null || !user.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user does not own resource");
        }
    }

    private void ensureCouponLockedToOrder(UserCoupon coupon, Long orderId) {
        if (coupon.getOrder() == null || coupon.getOrder().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon is not locked to an order");
        }
        if (!coupon.getOrder().getId().equals(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon does not belong to order");
        }
    }

    private void ensureCouponOwner(UserCoupon coupon, Long userId) {
        if (coupon.getUser() == null || coupon.getUser().getId() == null || !coupon.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "coupon does not belong to user");
        }
    }

    private void ensureCouponBelongsOrUnassigned(UserCoupon coupon, Long orderId) {
        if (coupon.getOrder() != null && coupon.getOrder().getId() != null
                && !coupon.getOrder().getId().equals(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "coupon does not belong to order");
        }
    }
}
