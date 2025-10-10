package com.iherbyou.ordering.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class OrderDetailDto {
    private Long id;

    private Integer orderStatusKey;
    private Integer subtotal;
    private Integer deliveryFee;
    private Integer discount;
    private Integer totalPrice;
    private String customsInfo;

    private LocalDateTime orderDate;

    // 배송(있을 때만)
    private String deliveryCompany;
    private String trackingNumber;
    private LocalDateTime delStartAt;
    private LocalDateTime delCompleteAt;
    private Integer deliveryStatusKey;

    // 라인아이템들
    private List<OrderItemDto> items;

}
