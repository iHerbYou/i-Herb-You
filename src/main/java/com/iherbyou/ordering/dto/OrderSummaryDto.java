package com.iherbyou.ordering.dto;

import com.iherbyou.ordering.Order;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class OrderSummaryDto {
    private Long id;
    private LocalDateTime orderDate;

    private Integer subtotal;
    private Integer deliveryFee;
    private Integer discount;
    private Integer totalPrice;

    private Integer orderStatusKey;

}