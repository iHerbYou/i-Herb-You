package com.iherbyou.catalog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant; // 옵션(SKU) 1:1

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer amount = 0; // 0보다 항상 크거나 같아야 하는데 check 옵션 필요?

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    private LocalDateTime restockedAt; // 입고일

}
