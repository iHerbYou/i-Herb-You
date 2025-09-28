package com.iherbyou.banner.repository;

import com.iherbyou.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    boolean existsByBannerName(String bannerName);
    boolean existsBySortOrder(Integer sortOrder);
}