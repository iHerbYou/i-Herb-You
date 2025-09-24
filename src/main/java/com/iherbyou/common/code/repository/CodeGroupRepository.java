package com.iherbyou.common.code.repository;

import com.iherbyou.common.code.entity.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {

    // value 필드로 그룹 찾기
    Optional<CodeGroup> findByValue(Integer value);

    // 활성 그룹만 찾기
    Optional<CodeGroup> findByValueAndIsActiveTrue(Integer value);

    // value 존재 여부 확인
    boolean existsByValue(Integer value);

    // 모든 활성 그룹 조회 (정렬 순서대로)
    List<CodeGroup> findByIsActiveTrueOrderBySortOrder();

    // displayName으로 검색
    Optional<CodeGroup> findByDisplayName(String displayName);
}