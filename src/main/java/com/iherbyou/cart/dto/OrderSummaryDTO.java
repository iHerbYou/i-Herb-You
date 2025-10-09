package com.iherbyou.cart.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Integer selectedItemCount;
    private Integer subTotal;
    private Integer shippingFee;
    private Integer totalAmount;
}