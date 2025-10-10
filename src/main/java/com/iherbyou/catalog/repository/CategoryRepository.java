package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 특정 부모 카테고리의 자식 카테고리 조회
    List<Category> findByParentId(Long parentId);

    Optional<Category> findByName(String name);

    // 카테고리 top3 조회 (자동완성용)
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN c.productCategories pc " +
            "LEFT JOIN pc.product p " +
            "WHERE c.name LIKE CONCAT('%', :keyword, '%') " +
            "GROUP BY c.id " +
            "ORDER BY COALESCE(SUM(p.sales), 0) DESC")
    List<Category> findTopCategoriesByKeyword(@Param("keyword") String keyword, Pageable pageable);

}