package com.iherbyou.promotion.point.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.promotion.point.PointCodeValues;
import com.iherbyou.promotion.point.repository.PointHistoryRepository;
import com.iherbyou.promotion.point.repository.PointRepository;
import com.iherbyou.promotion.policy.PromotionPolicyProperties;
import com.iherbyou.user.entity.Point;
import com.iherbyou.user.entity.PointHistory;
import com.iherbyou.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionPointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CodeService codeService;
    private final PromotionPolicyProperties policyProperties;

    public Optional<PointHistory> grantReviewPoints(User user, Long reviewId, boolean containsImage) {
        Objects.requireNonNull(user, "user must not be null");
        if (reviewId == null) {
            return Optional.empty();
        }
        int textPoint = policyProperties.getReviewPoint().getTextPoint();
        int imagePoint = policyProperties.getReviewPoint().getImagePoint();
        if (containsImage) {
            return grantImageReviewPoints(user, reviewId, textPoint, imagePoint);
        }
        if (textPoint <= 0) {
            return Optional.empty();
        }
        if (pointHistoryRepository
                .findFirstByPointTypeCodeValueAndRelatedReviewId(PointCodeValues.EARN_REVIEW_TEXT, reviewId)
                .isPresent()) {
            return Optional.empty();
        }
        return createEarnHistory(user, textPoint, PointCodeValues.EARN_REVIEW_TEXT,
                "리뷰 텍스트 포인트", reviewId, null,
                expiryAfterDays(policyProperties.getReviewPoint().getValidityDays()));
    }

    public Optional<PointHistory> grantOrderCompletionPoints(User user, Long orderId, int paymentAmount) {
        Objects.requireNonNull(user, "user must not be null");
        if (orderId == null || paymentAmount <= 0) {
            return Optional.empty();
        }
        if (pointHistoryRepository
                .findFirstByPointTypeCodeValueAndRelatedOrderId(PointCodeValues.EARN_ORDER_COMPLETION, orderId)
                .isPresent()) {
            return Optional.empty();
        }
        int percent = policyProperties.getOrderPoint().getPercent();
        if (percent <= 0) {
            return Optional.empty();
        }
        int points = calculatePercentagePoint(paymentAmount, percent, policyProperties.getOrderPoint().isRoundDown());
        if (points <= 0) {
            return Optional.empty();
        }
        return createEarnHistory(user, points, PointCodeValues.EARN_ORDER_COMPLETION,
                "구매 확정 포인트", null, orderId,
                expiryAfterDays(policyProperties.getOrderPoint().getValidityDays()));
    }

    public void usePointsForOrder(User user, int amount, Long orderId, int orderAmount) {
        if (amount <= 0) {
            return;
        }
        ensurePointMinimumOrder(orderAmount);
        Point point = getOrCreatePoint(user);
        point.spend(amount);
        saveHistory(point, user, -amount, PointCodeValues.USE_ORDER_PAYMENT,
                "주문 포인트 사용", null, orderId, null);
    }

    public void restorePointsFromOrder(User user, int amount, Long orderId) {
        if (amount <= 0) {
            return;
        }
        Point point = getOrCreatePoint(user);
        point.restore(amount);
        saveHistory(point, user, amount, PointCodeValues.RESTORE_ORDER_CANCEL,
                "주문 취소 포인트 복구", null, orderId,
                expiryAfterDays(policyProperties.getOrderPoint().getValidityDays()));
    }

    public void expirePoints() {
        LocalDateTime now = LocalDateTime.now();
        pointHistoryRepository.findByExpiredFalseAndExpiresAtBefore(now)
                .forEach(history -> {
                    int amount = Math.max(history.getAmount(), 0);
                    if (amount <= 0) {
                        history.markExpired();
                        return;
                    }
                    Point point = getOrCreatePoint(history.getUser());
                    point.expire(amount);
                    saveHistory(point, history.getUser(), -amount, PointCodeValues.EXPIRE,
                            "포인트 만료", null, null, null);
                    history.markExpired();
                });
    }

    private Optional<PointHistory> grantImageReviewPoints(User user, Long reviewId, int textPoint, int imagePoint) {
        if (imagePoint <= 0) {
            return Optional.empty();
        }
        if (pointHistoryRepository
                .findFirstByPointTypeCodeValueAndRelatedReviewId(PointCodeValues.EARN_REVIEW_IMAGE, reviewId)
                .isPresent()) {
            return Optional.empty();
        }
        Optional<PointHistory> textHistory = pointHistoryRepository
                .findFirstByPointTypeCodeValueAndRelatedReviewId(PointCodeValues.EARN_REVIEW_TEXT, reviewId);
        int delta = textHistory.map(PointHistory::getAmount)
                .map(earned -> imagePoint - earned)
                .orElse(imagePoint);
        if (delta <= 0) {
            return Optional.empty();
        }
        return createEarnHistory(user, delta, PointCodeValues.EARN_REVIEW_IMAGE,
                "리뷰 이미지 포인트", reviewId, null,
                expiryAfterDays(policyProperties.getReviewPoint().getValidityDays()));
    }

    private Optional<PointHistory> createEarnHistory(User user, int amount, int codeValue,
                                                     String reason, Long reviewId, Long orderId,
                                                     LocalDateTime expiresAt) {
        if (amount <= 0) {
            return Optional.empty();
        }
        Point point = getOrCreatePoint(user);
        point.earn(amount);
        PointHistory history = saveHistory(point, user, amount, codeValue, reason, reviewId, orderId, expiresAt);
        return Optional.of(history);
    }

    private PointHistory saveHistory(Point point, User user, int amount, int codeValue, String reason,
                                     Long reviewId, Long orderId, LocalDateTime expiresAt) {
        Code code = requireCode(PointCodeValues.GROUP_POINT_EVENT, codeValue);
        PointHistory history = PointHistory.builder()
                .user(user)
                .amount(amount)
                .balanceAfter(point.getBalance())
                .pointTypeCode(code)
                .reason(reason)
                .relatedReviewId(reviewId)
                .relatedOrderId(orderId)
                .expiresAt(expiresAt)
                .build();
        return pointHistoryRepository.save(history);
    }

    private Point getOrCreatePoint(User user) {
        return pointRepository.findByUserId(user.getId())
                .orElseGet(() -> pointRepository.save(Point.builder()
                        .user(user)
                        .build()));
    }

    private LocalDateTime expiryAfterDays(int days) {
        if (days <= 0) {
            return null;
        }
        return LocalDateTime.now().plusDays(days);
    }

    private int calculatePercentagePoint(int paymentAmount, int percent, boolean roundDown) {
        double raw = paymentAmount * percent / 100.0;
        return roundDown ? (int) Math.floor(raw) : (int) Math.round(raw);
    }

    private Code requireCode(int groupValue, int codeValue) {
        Code code = codeService.getCode(groupValue, codeValue);
        if (code == null) {
            throw new IllegalStateException("필요한 코드가 정의되어 있지 않습니다: " + groupValue + "-" + codeValue);
        }
        return code;
    }

    private void ensurePointMinimumOrder(int orderAmount) {
        int threshold = policyProperties.getPointMinOrderAmount();
        if (threshold <= 0) {
            return;
        }
        if (orderAmount < threshold) {
            throw new IllegalStateException("포인트는 " + threshold + "원 이상 주문에서만 사용할 수 있습니다.");
        }
    }
}
