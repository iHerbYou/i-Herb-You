package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminOrderLineItemDto {

    private final Long orderProductId;
    private final Long productId;
    private final Long productVariantId;
    private final String productName;
    private final String variantName;
    private final Integer quantity;
    private final Integer unitPrice;
    private final Integer subtotal;
}
