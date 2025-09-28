package com.iherbyou.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBannerResponse {
    private Long id;
    private String bannerName;
    private String imageUrl;
    private Integer sortOrder;
    private String message;
}
