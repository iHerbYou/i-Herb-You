package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 결제 id

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false, unique = true)    // 주문 id
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_id", nullable = false)   // 결제 상태 코드 id
    private Code paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false) // 결제 방법 코드 id
    private Code paymentMethod;

    @Column(nullable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime paymentDate;  // 결제 일시

    @Column(nullable = false,
            columnDefinition = "BIGINT CHECK (payment_amount >= 0)")
    private Long paymentAmount; // 결제 금액

}
