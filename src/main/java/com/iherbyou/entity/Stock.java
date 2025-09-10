package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productVariantId", nullable = false)
    private ProductVariant productVariant;

    @Column
    private Integer amount;

    protected Stock() {
    }

    public Stock(ProductVariant productVariant, Integer amount) {
        this.productVariant = productVariant;
        this.amount = amount;
    }
}
