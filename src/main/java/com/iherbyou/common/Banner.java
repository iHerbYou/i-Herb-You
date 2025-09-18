package com.iherbyou.common;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 광고 id

    @Column(length = 50)
    private String bannerName; // 광고명

    @Column
    private String imageUrl; // 이미지 url

}