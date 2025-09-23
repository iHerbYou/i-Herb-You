package com.iherbyou.ordering.entity;

import com.iherbyou.common.code.entity.Code;
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
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code_id", nullable = false)
    private Code statusCode; // 환불 상태 코드 (REQUESTED 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_code_id", nullable = false)
    private Code reasonCode; // 환불 사유 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_option_code_id", nullable = false)
    private Code deliveryOptionCode; // 환불 배송/수거 방식 코드

    @Column(nullable = false)
    private BigDecimal amount; // 환불 금액

    @Column(nullable = false)
    private LocalDateTime requestedAt; // 환불 요청 시각

    @Column
    private LocalDateTime completedAt; // 환불 완료 시각

    public void markStatus(Code status) {
        this.statusCode = status;
    }

    public void markCompleted(Code status, LocalDateTime completedAt) {
        this.statusCode = status;
        this.completedAt = completedAt;
    }
}
