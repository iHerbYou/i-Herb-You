package com.iherbyou.ordering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DeliveryRegisterRequest {
    @NotBlank
    @Size(max = 100)
    private String deliveryCompany;

    @NotBlank
    @Size(max = 100)
    private String trackingNumber;   // 123-456-789

}