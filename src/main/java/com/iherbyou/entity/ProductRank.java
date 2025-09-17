package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 랭킹 id

    // 어떤 상품의 랭킹인지
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)  // 상품 id
    private Product product;

    @Column
    private Integer recentSales;    // 최근 판매량

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

}