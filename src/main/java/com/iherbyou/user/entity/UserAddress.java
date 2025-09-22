package com.iherbyou.user.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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
    private String recipient; // 택배 수령인

    @Column(nullable = false, length = 30)
    private String phone; // 수령인 전화번호

    @Column(nullable = false)
    private String zipcode; // 우편번호

    @Column(nullable = false)
    private String address; // 주소

    @Column
    private String addressDetail; // 상세 주소 (null 가능)

    @Column
    private boolean isDefault; // 기본 배송지 여부 0=아님, 1=기본

}
