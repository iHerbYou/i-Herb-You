package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Product;
import com.iherbyou.catalog.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {
    void deleteByProduct(Product product);
}
