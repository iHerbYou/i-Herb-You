package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 100)
    private String variantName;

    @Column
    private Integer listPrice;

    @Column
    private Integer salePrice;

    @Column(length = 50)
    private String size;

    @Column
    private Integer volume;

    @Column(length = 50)
    private String upcCode;

    @Column(length = 20)
    private String pillSize;

    @Lob
    @Column(columnDefinition = "TEXT") // 긴 텍스트(예: 수천~수만 글자)
    private String nutritionFacts;

    @Column
    private Integer maxQtyPerOrder;

    @Column
    private LocalDateTime restockEta;
}
