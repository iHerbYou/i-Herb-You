package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Entity
@Getter
@Setter
@ToString
public class UserAddress {

    @Id
    @GeneratedValue
    private Long addressId;

    @Column(nullable = false, length = 20)
    private String recipient;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 255)
    private String postcode;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 255)
    private String addressDetail;

    @Column
    private boolean isDefault;

    // FK (user_id 생성)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User userId;


    public UserAddress() {}

    public UserAddress(Long addressId, String recipient, String phone, String postcode, String address, boolean isDefault, String addressDetail, User userId) {
        this.addressId = addressId;
        this.recipient = recipient;
        this.phone = phone;
        this.postcode = postcode;
        this.address = address;
        this.isDefault = isDefault;
        this.addressDetail = addressDetail;
        this.userId = userId;
    }
}
