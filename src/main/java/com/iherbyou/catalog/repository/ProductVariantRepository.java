package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProduct(Product product);
}
