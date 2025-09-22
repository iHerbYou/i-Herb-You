package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // N+1 문제 해결을 위한 EntityGraph 사용
    @EntityGraph(attributePaths = {"productVariants", "productVariants.stock", "productImgs", "reviews"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

}
