package com.iherbyou.banner.mapper;

import com.iherbyou.banner.dto.AddBannerRequest;
import com.iherbyou.banner.dto.AddBannerResponse;
import com.iherbyou.banner.entity.Banner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BannerMapper {

    public Banner toEntity(AddBannerRequest request) {
        return Banner.builder()
                .bannerName(request.getBannerName())
                .imageUrl(request.getImageUrl())
                .sortOrder(request.getSortOrder())
                .build();
    }

    public AddBannerResponse toResponse(Banner banner) {
        return AddBannerResponse.builder()
                .id(banner.getId())
                .bannerName(banner.getBannerName())
                .imageUrl(banner.getImageUrl())
                .sortOrder(banner.getSortOrder())
                .build();
    }

    public List<AddBannerResponse> toResponseList(List<Banner> banners) {
        return banners.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}