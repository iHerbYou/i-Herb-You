package com.iherbyou.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name; // 카테고리 이름

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; // 상위 카테고리

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>(); // 하위 카테고리

    @OneToMany(mappedBy = "category")
    private List<ProductCategory> productCategories = new ArrayList<>(); // 카테고리에 속한 상품들

    public void addChildCategory(Category child) {
        children.add(child);
        child.setParent(this);
    }

}