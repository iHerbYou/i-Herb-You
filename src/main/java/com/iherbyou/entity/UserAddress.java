package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UserAddress {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String recipient; //수령인

    @Column(nullable = false, length = 30)
    private String phone; //수령인 전화번호

    @Column(nullable = false)
    private String postcode; //우편번호

    @Column(nullable = false)
    private String address; // 기본주소

    @Column
    private String addressDetail; // 상세 주소 (null 가능)

    @Column
    private boolean isDefault; // 기본 배송지 여부 0=아님, 1=기본

}
