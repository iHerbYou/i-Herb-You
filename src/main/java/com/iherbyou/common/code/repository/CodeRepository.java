package com.iherbyou.common.code.repository;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.entity.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    // 특정 코드 그룹의 모든 활성 코드 조회 (정렬 순서대로)
    List<Code> findByCodeGroupAndIsActiveTrueOrderBySortOrder(CodeGroup codeGroup);

    // 코드 그룹 value와 코드 value로 코드 조회
    @Query("SELECT c FROM Code c WHERE c.codeGroup.value = :groupValue AND c.value = :codeValue")
    Optional<Code> findByGroupValueAndCodeValue(@Param("groupValue") Integer groupValue, @Param("codeValue") Integer codeValue);

    // 활성 코드만 조회 (유효 기간도 고려)
    @Query("SELECT c FROM Code c WHERE c.codeGroup.value = :groupValue AND c.value = :codeValue AND c.isActive = true " +
            "AND (c.validFrom IS NULL OR c.validFrom <= CURRENT_TIMESTAMP) " +
            "AND (c.validTo IS NULL OR c.validTo >= CURRENT_TIMESTAMP)")
    Optional<Code> findActiveByGroupValueAndCodeValue(@Param("groupValue") Integer groupValue, @Param("codeValue") Integer codeValue);

    // 특정 코드 그룹의 모든 코드 조회 (활성/비활성 무관)
    List<Code> findByCodeGroupOrderBySortOrder(CodeGroup codeGroup);

    // 활성 코드 존재 여부 확인
    @Query("SELECT COUNT(c) > 0 FROM Code c WHERE c.codeGroup.value = :groupValue AND c.value = :codeValue AND c.isActive = true " +
            "AND (c.validFrom IS NULL OR c.validFrom <= CURRENT_TIMESTAMP) " +
            "AND (c.validTo IS NULL OR c.validTo >= CURRENT_TIMESTAMP)")
    boolean existsActiveByGroupValueAndCodeValue(@Param("groupValue") Integer groupValue, @Param("codeValue") Integer codeValue);

    // ID로 활성 코드 조회 (유효 기간 고려)
    @Query("SELECT c FROM Code c WHERE c.id = :id AND c.isActive = true " +
            "AND (c.validFrom IS NULL OR c.validFrom <= CURRENT_TIMESTAMP) " +
            "AND (c.validTo IS NULL OR c.validTo >= CURRENT_TIMESTAMP)")
    Optional<Code> findActiveById(@Param("id") Long id);

    // displayName으로 검색
    List<Code> findByDisplayNameContainingIgnoreCase(String displayName);

}