package com.iherbyou.ordering.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DeliveryRegisterRequest {
    private String deliveryCompany;
    private String trackingNumber;   // 123-456-789

}