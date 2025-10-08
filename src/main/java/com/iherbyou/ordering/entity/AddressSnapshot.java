package com.iherbyou.ordering.entity;

import com.iherbyou.user.entity.UserAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class AddressSnapshot {

    @Column(name = "recipient", length = 20)
    private String recipient;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "zipcode", length = 10)
    private String zipcode;

    @Column(name = "address_line1", length = 200)
    private String addressLine1;

    @Column(name = "address_line2", length = 200)
    private String addressLine2;

    public static AddressSnapshot from(UserAddress userAddress) {
        Objects.requireNonNull(userAddress, "userAddress must not be null");

        return AddressSnapshot.builder()
                .recipient(userAddress.getRecipient())
                .phone(userAddress.getPhone())
                .zipcode(userAddress.getZipcode())
                .addressLine1(userAddress.getAddress())
                .addressLine2(userAddress.getAddressDetail())
                .build();
    }

    public AddressSnapshot copy() {
        return AddressSnapshot.builder()
                .recipient(this.recipient)
                .phone(this.phone)
                .zipcode(this.zipcode)
                .addressLine1(this.addressLine1)
                .addressLine2(this.addressLine2)
                .build();
    }
}
