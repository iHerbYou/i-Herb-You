package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "category",
        indexes = {
                @Index(name = "idx_category_parent", columnList = "parent_id"),
                @Index(name = "idx_category_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_category_code", columnNames = {"code"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예: “VITAMINS”, “SPORTS”, …
    @Column(length = 50, nullable = false)
    private String name;

    // 내부 식별/URL 슬러그 등으로 활용
    @Column(length = 50, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",
            foreignKey = @ForeignKey(name = "fk_category_parent"))
    private Category parent;

    // 단순 정렬/표시 우선순위
    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;

    // 양방향이 꼭 필요하지 않다면 생략 가능
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();
}