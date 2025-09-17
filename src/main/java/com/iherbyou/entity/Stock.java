package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        name = "stock",
        indexes = @Index(name = "idx_stock_product_variant_id", columnList = "product_variant_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_stock_product_variant_id", columnNames = {"product_variant_id"})
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", nullable = false)                         // 재고 id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)           // 옵션(SKU) 1:1
    private ProductVariant productVariant;

    // NOT NULL, DEFAULT 0
    @Column(name = "amount", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer amount = 0;

    //NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    private LocalDateTime updatedAt;

    //선택 컬럼 (NULL 허용)
    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    // 생성 편의 생성자
    public Stock(ProductVariant productVariant, Integer amount) {
        this.productVariant = productVariant;
        this.amount = (amount == null ? 0 : amount);
    }
}
