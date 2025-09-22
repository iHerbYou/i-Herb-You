package com.iherbyou.ordering;

import com.iherbyou.common.Code;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@Entity
public class Payment {

    @Id
    private Long id; // 결제 id (주문 id와 동일하게 사용)

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_code_id", nullable = false)
    private Code paymentStatusCode; // 결제 상태 코드 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_code_id", nullable = false)
    private Code paymentMethodCode; // 결제 방법 코드 id

    @Builder.Default
    @OneToMany(mappedBy = "payment")
    private List<Refund> refunds = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal paymentPrice; // 결제 금액

    @Column(nullable = false)
    private LocalDateTime requestedAt; // 결제 요청 시각

    @Column
    private LocalDateTime paidAt; // 결제 완료 시각

    public void markRequested(Code status, Code method, BigDecimal amount, LocalDateTime requestedAt) {
        this.paymentStatusCode = status;
        this.paymentMethodCode = method;
        this.paymentPrice = amount;
        this.requestedAt = requestedAt;
        this.paidAt = null;
    }

    public void markPaid(Code status, LocalDateTime paidAt) {
        this.paymentStatusCode = status;
        this.paidAt = paidAt;
    }

}