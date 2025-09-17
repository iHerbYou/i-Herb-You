package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        name = "order_product",
        indexes = {
                @Index(name = "idx_order_product_order", columnList = "order_id"),
                @Index(name = "idx_order_product_variant", columnList = "product_variant_id")
        }
)
@Check(constraints = "qty > 0") // 수량은 1 이상
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id", nullable = false)          // 주문 상세 고유 ID
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_op_order"))           // 상위 주문
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_op_product_variant")) // 주문한 옵션(SKU)
    private ProductVariant productVariant;

    @Column(name = "qty", nullable = false)                        // 수량 (NOT NULL, CHECK>0)
    private Integer qty;

    @Column(name = "unit_price_at_order", nullable = false)        // 단가(주문 시점)
    private Integer unitPriceAtOrder;

    @Column(name = "regular_price_at_order", nullable = false)     // 정가(주문 시점)
    private Integer regularPriceAtOrder;

    @Column(name = "subtotal", nullable = false)                   // 소계 = 단가 × 수량
    private Integer subtotal;

    public OrderProduct(Order order,
                        ProductVariant productVariant,
                        Integer qty,
                        Integer unitPriceAtOrder,
                        Integer regularPriceAtOrder) {
        this.order = order;
        this.productVariant = productVariant;
        this.qty = (qty == null || qty <= 0) ? 1 : qty;
        this.unitPriceAtOrder = nvl(unitPriceAtOrder);
        this.regularPriceAtOrder = nvl(regularPriceAtOrder);
        recalcSubtotal();
    }

    private static Integer nvl(Integer v) { return v == null ? 0 : v; }

    public void recalcSubtotal() {
        this.subtotal = unitPriceAtOrder * this.qty;
        if (this.subtotal < 0) this.subtotal = 0; // 오버플로/음수 방지(안전장치)
    }
}
