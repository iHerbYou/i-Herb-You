package com.iherbyou.ordering;

import com.iherbyou.common.Code;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 결제 id

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_code_id", nullable = false)
    private Code paymentStatusCode; // 결제 상태 코드 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_code_id", nullable = false)
    private Code paymentMethodCode; // 결제 방법 코드 id

    @OneToMany(mappedBy = "payment")
    private List<Refund> refunds = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime paymentDate;  // 결제 일시

    @Column(updatable = false)
    private LocalDateTime cancelDate; // 결제 취소일

    @Column(nullable = false, columnDefinition = "BIGINT CHECK (payment_price >= 0)")
    private BigDecimal paymentPrice; // 결제 금액

}
