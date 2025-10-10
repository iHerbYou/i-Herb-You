package com.iherbyou.promotion.point.dto;

import com.iherbyou.user.entity.PointHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointHistoryItemDto {

    private final Integer type;
    private final int amount;
    private final int balanceAfter;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private final boolean expired;
    private final Long orderId;
    private final Long reviewId;

    public static PointHistoryItemDto from(PointHistory history) {
        return PointHistoryItemDto.builder()
                .type(history.getPointTypeCode() != null ? history.getPointTypeCode().getValue() : null)
                .amount(history.getAmount())
                .balanceAfter(history.getBalanceAfter())
                .createdAt(history.getCreatedAt())
                .expiresAt(history.getExpiresAt())
                .expired(history.isExpired())
                .orderId(history.getRelatedOrderId())
                .reviewId(history.getRelatedReviewId())
                .build();
    }
}
