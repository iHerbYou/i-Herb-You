package com.iherbyou.promotion.point.service;

import com.iherbyou.ordering.entity.Order;
import com.iherbyou.ordering.repository.OrderRepository;
import com.iherbyou.promotion.point.repository.PointHistoryRepository;
import com.iherbyou.promotion.point.repository.PointRepository;
import com.iherbyou.user.entity.Point;
import com.iherbyou.user.entity.PointHistory;
import com.iherbyou.user.entity.User;
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
public class PromotionPointFacade {

    private final PromotionPointService promotionPointService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public Optional<PointHistory> grantReviewPoints(Long userId, Long reviewId, boolean containsImage) {
        User user = requireUser(userId);
        return promotionPointService.grantReviewPoints(user, reviewId, containsImage);
    }

    public Optional<PointHistory> grantOrderCompletionPoints(Long userId, Long orderId, int paymentAmount) {
        Order order = requireOrder(orderId);
        ensureOrderOwner(order, userId);
        return promotionPointService.grantOrderCompletionPoints(order.getUser(), orderId, paymentAmount);
    }

    public void usePoints(Long userId, Long orderId, int amount) {
        Order order = requireOrder(orderId);
        ensureOrderOwner(order, userId);
        int orderAmount = effectiveOrderAmount(order);
        try {
            promotionPointService.usePointsForOrder(order.getUser(), amount, orderId, orderAmount);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    public void restorePoints(Long userId, Long orderId, int amount) {
        Order order = requireOrder(orderId);
        ensureOrderOwner(order, userId);
        promotionPointService.restorePointsFromOrder(order.getUser(), amount, orderId);
    }

    public void expirePoints() {
        promotionPointService.expirePoints();
    }

    public int currentBalance(Long userId) {
        return pointRepository.findByUserId(userId)
                .map(Point::getBalance)
                .orElse(0);
    }

    public List<PointHistory> getHistories(Long userId) {
        requireUser(userId);
        return pointHistoryRepository.findByUserId(userId);
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    private Order requireOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
    }

    private void ensureOrderOwner(Order order, Long userId) {
        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "order does not belong to user");
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
