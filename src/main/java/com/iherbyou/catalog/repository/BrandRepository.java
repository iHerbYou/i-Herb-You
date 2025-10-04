package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
