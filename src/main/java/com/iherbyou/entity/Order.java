package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false) // 회원 id (FK, NOT NULL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_status_code_id", nullable = false)
    private Code orderStatusCode; // 주문 상태 코드 (예: 결제완료/배송중/취소 등) → Code 테이블 FK

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Delivery delivery;

    @Column(length = 50)
    private String customsInfo; // 개인 통관 번호

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate; // 주문일시

    @Column(nullable = false)
    private Integer subtotal; // 소계 금액 (상품 금액 합계. 할인/배송 제외)

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer deliveryFee;  // 배송비 (DEFAULT 0)

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer discount; // 할인액 (DEFAULT 0)

    @Column(nullable = false)
    private Integer totalPrice; // 최종 결제 금액

    /**
     * 소계+배송비-할인 재계산
     */
    public void recalcTotal() {
        int sub = (subtotal == null ? 0 : subtotal);
        int fee = (deliveryFee == null ? 0 : deliveryFee);
        int dc = (discount == null ? 0 : discount);
        int total = sub + fee - dc;
        this.totalPrice = Math.max(total, 0);
    }

}
