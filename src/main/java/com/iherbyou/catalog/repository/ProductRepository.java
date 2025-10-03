package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 카테고리별 상품 조회
    List<Product> findByProductCategories_CategoryId(Long categoryId, Pageable pageable);

    // 이름 검색
    List<Product> findByNameContaining(String keyword);

    // 품절 제외
    // TODO

}
