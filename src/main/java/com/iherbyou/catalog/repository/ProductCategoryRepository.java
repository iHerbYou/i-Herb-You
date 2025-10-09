package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    void deleteByProduct(Product product);
}
