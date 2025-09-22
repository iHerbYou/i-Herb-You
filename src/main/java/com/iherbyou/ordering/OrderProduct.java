package com.iherbyou.ordering;

import com.iherbyou.catalog.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue
    private Long id; // 주문 상세 고유 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 상위 주문

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant; // 주문한 옵션(SKU)

    @Column(nullable = false, columnDefinition = "INT CHECK (qty > 0)")
    private Integer qty; // 수량 (NOT NULL, CHECK>0)

    @Column(nullable = false)
    private Integer unitPriceAtOrder; // 단가 (주문 시점)

    @Column(nullable = false)
    private Integer regularPriceAtOrder; // 정가 (주문 시점)

    @Column(nullable = false)
    private Integer subtotal; // 소계 = 단가 × 수량

}