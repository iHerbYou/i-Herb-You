package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_code_id", nullable = false)
    private Code refundCode; // 환불 상태 나타내는 코드 (환불 요청, 승인, 거절, 진행중, 완료)

    @Column
    private LocalDateTime refundDate; // 환불 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_reason_option_code_id", nullable = false)
    private Code refundReasonOptionCode; // 환불 사유 옵션

    @Column
    private String refundReasonText; // 환불 사유 글

    @Column
    private BigDecimal refundPrice; // 환불 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_delivery_optioin_code_id", nullable = false)
    private Code refundDeliveryOptionCode; // 수거 요청, 직접 발송 (환불 방법)

}
