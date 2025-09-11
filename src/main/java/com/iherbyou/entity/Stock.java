package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productVariantId", nullable = false)
    private ProductVariant productVariant;

    @Column
    private Integer amount;

    //TODO
    public Stock(ProductVariant productVariant, Integer amount) {
        this.productVariant = productVariant;
        this.amount = amount;
    }
}
