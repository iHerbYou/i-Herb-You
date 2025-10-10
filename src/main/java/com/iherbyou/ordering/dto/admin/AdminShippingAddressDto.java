package com.iherbyou.ordering.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminShippingAddressDto {

    private final String recipient;
    private final String phone;
    private final String zipcode;
    private final String addressLine1;
    private final String addressLine2;
}
