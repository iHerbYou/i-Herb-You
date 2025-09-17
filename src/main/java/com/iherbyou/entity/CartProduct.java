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
        name = "cart_product",
        indexes = {
                @Index(name = "idx_cart_product_cart", columnList = "cart_id"),
                @Index(name = "idx_cart_product_variant", columnList = "product_variant_id")
        }
)
@Check(constraints = "qty > 0") // 수량은 1 이상
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_product_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_product_cart"))
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_product_variant"))
    private ProductVariant productVariant;

    @Column(name = "qty", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer qty = 1;

    @Column(name = "is_selected", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isSelected = true;

    public CartProduct(Cart cart, ProductVariant productVariant, Integer qty, Boolean isSelected) {
        this.cart = cart;
        this.productVariant = productVariant;
        this.qty = (qty == null || qty <= 0) ? 1 : qty;
        this.isSelected = (isSelected == null) ? true : isSelected;
    }
}
