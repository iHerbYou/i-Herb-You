package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminDeliveryInfoDto {

    private final Long deliveryId;
    private final Integer statusValue;
    private final String statusName;
    private final String company;
    private final String trackingNumber;
    private final String memo;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
}
