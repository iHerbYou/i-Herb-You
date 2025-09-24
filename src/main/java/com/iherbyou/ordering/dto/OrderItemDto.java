package com.iherbyou.ordering.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class OrderItemDto {
    private Long orderProductId;
    private Long productVariantId;
    private String productName;   // Product.name
    private String variantName;   // ProductVariant.variantName
    private Integer qty;
    private Integer unitPrice;    // 주문 시점 단가
    private Integer subtotal;     // qty * unitPrice

}