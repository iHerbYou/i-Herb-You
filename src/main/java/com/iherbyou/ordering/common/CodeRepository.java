package com.iherbyou.ordering.common;

import com.iherbyou.common.code.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    @Query("""
        select c from Code c 
        join c.codeGroup g 
        where g.groupKey = :groupKey and c.value = :codeKey
    """)
    Optional<Code> find(String groupKey, String codeKey);
}