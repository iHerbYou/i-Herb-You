package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 배송 id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 주문 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)
    private Code code; // 배송 상태 코드 id

    @OneToOne(fetch = FetchType.LAZY) // 단방향 연결
    @JoinColumn(name = "address_id", nullable = false)
    private UserAddress userAddress; // 주소id

    @Column(length = 50, unique = true)
    private Integer trackingNumber; // 송장번호

    @Column(length = 100)
    private String deliveryCompany; // 택배사

    @Column
    private String delMemo; // 배송 메모

    @Column
    private LocalDateTime delStartAt; // 배송 시작일

    @Column
    private LocalDateTime delCompleteAt; // 배송 완료일

}
