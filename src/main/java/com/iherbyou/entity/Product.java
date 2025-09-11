package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "fk_product_brand"))
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;

    @Column(name = "product_name", length = 50)
    private String name;

    @Column(name = "product_code", length = 50)
    private String code;

    @Column(name = "upc_code", length = 255)
    private String upcCode;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "sale_start_date")
    private LocalDate saleStartDate;
}
