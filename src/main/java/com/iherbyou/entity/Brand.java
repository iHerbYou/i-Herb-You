package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        name = "brand",
        uniqueConstraints = @UniqueConstraint(name = "uk_brand_name", columnNames = "brand_name")
)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long id;

    @Column(name = "brand_name", length = 255, nullable = false, unique = true)
    private String name;
}
