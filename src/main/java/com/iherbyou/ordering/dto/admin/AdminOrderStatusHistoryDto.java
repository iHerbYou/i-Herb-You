package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminOrderStatusHistoryDto {

    private final String fromStatus;
    private final String toStatus;
    private final String source;
    private final String actor;
    private final String reason;
    private final LocalDateTime changedAt;
}
