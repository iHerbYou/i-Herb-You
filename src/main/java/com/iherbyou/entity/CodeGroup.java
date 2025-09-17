package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class CodeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 코드 그룹 ID

    @Column(nullable = false, unique = true)
    private String groupKey; // 그룹 키

    @Column(length = 100, nullable = false)
    private String groupName; // 그룹 표시명

    @Column
    private String description; // 설명

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive; // 그룹 사용 여부

    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer sortOrder; // 그룹 정렬

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 시각

}