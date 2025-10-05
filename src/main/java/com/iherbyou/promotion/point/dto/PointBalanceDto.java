package com.iherbyou.promotion.point.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointBalanceDto {

    private final Long userId;
    private final Integer balance;

    public static PointBalanceDto of(Long userId, Integer balance) {
        return PointBalanceDto.builder()
                .userId(userId)
                .balance(balance)
                .build();
    }
}
