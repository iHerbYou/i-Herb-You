package com.iherbyou.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Banner")

public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "banner_id", nullable = false)
    private Long id;
    @Column(name = "banner_name", length = 50)
    private String bannerName;
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    public Banner() {
    }
}