package com.iherbyou.banner.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_sort_order", columnList = "sortOrder"),
        @Index(name = "idx_banner_name", columnList = "bannerName"),
        @Index(name = "idx_image_url", columnList = "imageUrl")
})
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String bannerName;

    @Column(nullable = false)
    private String imageUrl;

    @Column(unique = true, nullable = false)
    private Integer sortOrder;
}