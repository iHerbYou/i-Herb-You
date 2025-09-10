package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productVariantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column
    private Integer price;

    @Column(length = 100)
    private String size;

    @Column
    private Integer weight;

    @Column
    private Integer volume;

    protected ProductVariant() {}

    public ProductVariant(Product product, Integer price, String size, Integer weight, Integer volume) {
        this.product = product;
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.volume = volume;
    }
}

