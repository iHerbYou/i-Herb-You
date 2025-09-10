package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "brand",
        indexes = {
                @Index(name = "idx_brand_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brand_code", columnNames = {"code"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 내부 식별용, 노출용
    @Column(length = 50, nullable = false)
    private String name;

    // 고유 코드
    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;
}