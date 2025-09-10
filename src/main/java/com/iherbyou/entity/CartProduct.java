package com.iherbyou.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "Cart_product" )
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "cart_product_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "product_variant_id", nullable = false)
    private ProductVariant productVariantId;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "is_selected",nullable = false)
    private Boolean isSelected;

    public CartProduct() {}
}
