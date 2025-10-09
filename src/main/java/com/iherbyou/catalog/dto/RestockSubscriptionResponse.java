package com.iherbyou.catalog.dto;

import com.iherbyou.catalog.entity.RestockSubscription;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RestockSubscriptionResponse {

    private Long id;
    private String userEmail;
    private Long productVariantId;
    private String productName;
    private String variantName;
    private Boolean isActive;
    private LocalDateTime subscribedAt;
    private LocalDateTime lastNotifiedAt;

    public static RestockSubscriptionResponse from(RestockSubscription entity) {
        return RestockSubscriptionResponse.builder()
                .id(entity.getId())
                .userEmail(entity.getUser().getEmail())
                .productVariantId(entity.getProductVariant().getId())
                .productName(entity.getProductVariant().getProduct().getName())
                .variantName(entity.getProductVariant().getVariantName())
                .isActive(entity.getIsActive())
                .subscribedAt(entity.getSubscribedAt())
                .lastNotifiedAt(entity.getLastNotifiedAt())
                .build();
    }

}
