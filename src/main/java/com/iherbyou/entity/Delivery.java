package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 배송 id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)  // 주문 id
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)   // 배송 상태 코드 id
    private Code code;

    @OneToOne(fetch = FetchType.LAZY)   // 단방향 연결
    @JoinColumn(name = "address_id", nullable = false)    // 주소id
    private UserAddress userAddress;

    @Column(length = 50, unique = true)
    private Integer trackingNumber; //송장번호

    @Column(length = 100)
    private String deliveryCompany; //택배사

    @Column
    private String delMemo; //배송 메모

    @Column
    private LocalDateTime delStartAt; // 배송 시작일

    @Column
    private LocalDateTime delCompleteAt;    // 배송 완료일

}
