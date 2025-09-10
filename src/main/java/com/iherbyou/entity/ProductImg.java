package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 255)
    private String altText;

    @Column
    private Integer sortIdx;

    protected ProductImg() {
    }

    public ProductImg(Product product, String imageUrl, String altText, Integer sortIdx) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.sortIdx = sortIdx;
    }

}
