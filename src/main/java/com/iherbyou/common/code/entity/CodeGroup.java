package com.iherbyou.common.code.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CodeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer value; // 10, 20, 30

    @Column(length = 100, nullable = false)
    private String displayName; // "사용자 관련", "쿠폰 관련", "배송관련"

    @Column
    private String description; // 설명

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isActive = true; // 그룹 사용 여부

    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer sortOrder; // 그룹 정렬

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성 시각

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 시각

    @OneToMany(mappedBy = "codeGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Code> codes = new ArrayList<>();

    /**
     * Business Methods
     */
    public void updateDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateActiveStatus(Boolean isActive) {
        this.isActive = isActive;
    }

}