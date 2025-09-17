package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 환불 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)   // 결제 id
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)  // 코드 id
    private Code code;

    @Column
    private LocalDateTime refundDate;   // 환불 일자

    @Column(length = 255)
    private String refundReason;    // 환불 사유

    @Column
    private Integer refundAmount;   // 환불 금액

}
