package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // N+1 문제 해결을 위한 EntityGraph 사용
    @EntityGraph(attributePaths = {"productVariants", "productVariants.stock", "productImgs", "reviews"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    // 카테고리 기반 상품 조회 (페이징)
    @EntityGraph(attributePaths = {"brand", "productImgs", "reviews", "productVariants", "productVariants.stock"})
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.productCategories pc " +
            "WHERE pc.category.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // 여러 카테고리 기반 상품 조회 (페이징) - 하위 카테고리 포함용
    @EntityGraph(attributePaths = {"brand", "productImgs", "reviews", "productVariants", "productVariants.stock"})
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.productCategories pc " +
            "WHERE pc.category.id IN :categoryIds")
    Page<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    // 카테고리 기반 베스트셀러 조회
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN p.productCategories pc " +
            "WHERE pc.category.id = :categoryId " +
            "ORDER BY p.sales DESC")
    Page<Product> findByCategoryIdOrderBySalesDesc(@Param("categoryId") Long categoryId, Pageable pageable);

    // 여러 카테고리 기반 베스트셀러 조회 - 하위 카테고리 포함용
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN p.productCategories pc " +
            "WHERE pc.category.id IN :categoryIds " +
            "ORDER BY p.sales DESC")
    Page<Product> findByCategoryIdsOrderBySalesDesc(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    // 전체 베스트셀러 조회
    @Query("SELECT p FROM Product p ORDER BY p.sales DESC")
    Page<Product> findAllOrderBySalesDesc(Pageable pageable);

    // 최근 출시된 상품 조회
    @EntityGraph(attributePaths = {"brand", "productImgs", "reviews", "productVariants", "productVariants.stock"})
    @Query("SELECT p FROM Product p ORDER BY p.saleStartDate DESC")
    Page<Product> findAllOrderBySaleStartDateDesc(Pageable pageable);

    // 높은 별점 순 (같으면 판매량 순) 조회
    @EntityGraph(attributePaths = {"brand", "productImgs", "reviews", "productVariants", "productVariants.stock"})
    @Query("SELECT p FROM Product p ORDER BY p.avgRating DESC, p.sales DESC")
    Page<Product> findAllOrderByAvgRatingDesc(Pageable pageable);

    // 상품명 or 브랜드명 검색
    @EntityGraph(attributePaths = {"brand", "productImgs", "reviews", "productVariants", "productVariants.stock"})
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.brand b " +
            "WHERE p.name LIKE CONCAT('%', :keyword, '%') " +
            "   OR b.name LIKE CONCAT('%', :keyword, '%')")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 상품 top7 (자동완성용)
    @EntityGraph(attributePaths = {"brand"})
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.brand b " +
            "WHERE p.name LIKE CONCAT('%', :keyword, '%') " +
            "   OR b.name LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.sales DESC")
    List<Product> findTopProductsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}