package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 특정 부모 카테고리의 자식 카테고리 조회
    List<Category> findByParentId(Long parentId);

}
