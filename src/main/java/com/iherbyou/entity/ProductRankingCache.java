package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "product_ranking_cache",
        indexes = {
                @Index(name = "idx_prc_product", columnList = "product_id"),
                @Index(name = "idx_prc_snapshot", columnList = "snapshot_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prc_product_date_source",
                        columnNames = {"product_id", "snapshot_date", "source"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProductRankingCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 상품의 랭킹인지
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id",
            foreignKey = @ForeignKey(name = "fk_prc_product"))
    private Product product;

    // 일자 스냅샷(일 단위 집계 가정)
    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    // 랭크 (1위, 2위, …)
    @Column(nullable = false)
    private Integer rank;

    // 점수(인기도, 판매지수 등 가중치 합산값)
    @Column(precision = 12, scale = 4)
    private BigDecimal score;

    // 데이터 소스(예: “GLOBAL”, “KR”, “NEW_ARRIVALS”…)
    @Column(length = 40, nullable = false)
    private String source = "GLOBAL";

    // 캐시 최신화 시각
    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}