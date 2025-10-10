package com.iherbyou.catalog.dto;

import com.iherbyou.catalog.entity.RestockSubscription;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RestockSubscriptionDto {

    private Long id;
    private Long userId;
    private Long variantId;
    private Boolean isActive;
    private LocalDateTime subscribedAt;

    public static RestockSubscriptionDto from(RestockSubscription entity) {
        return RestockSubscriptionDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .variantId(entity.getProductVariant().getId())
                .isActive(entity.getIsActive())
                .subscribedAt(entity.getSubscribedAt())
                .build();
    }

}