package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Entity
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String altText;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer sortIdx;   // 정렬 순서 (낮을수록 우선)

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isPrimary; // 대표 이미지 여부 (0=일반, 1=대표)
}
