package com.iherbyou.ordering.dto;

import com.iherbyou.ordering.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class DeliveryDto {
    private String deliveryCompany;
    private String trackingNumber;
    private LocalDateTime delStartAt;
    private LocalDateTime delCompleteAt;
    private String statusKey;

    public static DeliveryDto from(Delivery d) {
        return DeliveryDto.builder()
                .deliveryCompany(d.getDeliveryCompany())
                .trackingNumber(d.getTrackingNumber())
                .delStartAt(d.getDelStartAt())
                .delCompleteAt(d.getDelCompleteAt())
                .statusKey(d.getCode().getCodeKey())
                .build();
    }

}