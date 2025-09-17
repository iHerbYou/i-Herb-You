package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id"),
                @Index(name = "idx_order_status_date", columnList = "order_status_id, order_date")
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)                            // 주문 id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_user"))                  // 회원 id (FK, NOT NULL)
    private User user;

    // 주문 상태 코드 (예: 결제완료/배송중/취소 등) → Code 테이블 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_status_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_status_code"))
    private Code orderStatus;

    // Delivery는 order_id로 참조(양방향 필요 시만 유지)
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Delivery delivery;

    @Column(name = "customs_info", length = 50)                              // 통관정보(선택)
    private String customsInfo;

    @CreationTimestamp
    @Column(name = "order_date", nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")        // 주문일시
    private LocalDateTime orderDate;

    @Column(name = "subtotal", nullable = false)                             // 소계 금액
    private Integer subtotal;

    @Column(name = "delivery_fee", nullable = false,
            columnDefinition = "INT DEFAULT 0")                              // 배송비 (DEFAULT 0)
    private Integer deliveryFee = 0;

    @Column(name = "discount", nullable = false,
            columnDefinition = "INT DEFAULT 0")                              // 할인액 (DEFAULT 0)
    private Integer discount = 0;

    @Column(name = "total_amount", nullable = false)                         // 최종 결제 금액
    private Integer totalAmount;

    /** 소계+배송비-할인 재계산 */
    public void recalcTotal() {
        int sub = (subtotal == null ? 0 : subtotal);
        int fee = (deliveryFee == null ? 0 : deliveryFee);
        int dc  = (discount == null ? 0 : discount);
        int total = sub + fee - dc;
        this.totalAmount = Math.max(total, 0);
    }
}
