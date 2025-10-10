package com.iherbyou.user.dto;

import com.iherbyou.user.entity.UserAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAddressResponseDto {

    private final Long id;
    private final String recipient;
    private final String phone;
    private final String zipcode;
    private final String address;
    private final String addressDetail;
    private final boolean isDefault;

    public static UserAddressResponseDto from(UserAddress entity) {
        return UserAddressResponseDto.builder()
                .id(entity.getId())
                .recipient(entity.getRecipient())
                .phone(entity.getPhone())
                .zipcode(entity.getZipcode())
                .address(entity.getAddress())
                .addressDetail(entity.getAddressDetail())
                .isDefault(entity.isDefault())
                .build();
    }
}
