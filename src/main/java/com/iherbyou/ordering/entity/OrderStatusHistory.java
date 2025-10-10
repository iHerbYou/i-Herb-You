package com.iherbyou.ordering.entity;

import com.iherbyou.ordering.code.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 주문 상태 변경 이력. 멱등 키 기반으로 중복 전이를 차단하기 위한 outbox 유사 테이블.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "order_status_history",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_status_history_order_to_status_corr",
                        columnNames = {"order_id", "to_status", "correlation_id"}
                )
        })
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 40)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", length = 40, nullable = false)
    private OrderStatus toStatus;

    @Column(name = "source", length = 80)
    private String source;

    @Column(name = "actor", length = 80)
    private String actor;

    @Column(name = "reason", length = 200)
    private String reason;

    @Column(name = "correlation_id", length = 100, nullable = false)
    private String correlationId;

    @CreatedDate
    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;
}
