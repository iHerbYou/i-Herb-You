package com.iherbyou.common.code.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 코드 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_group_id", nullable = false)
    private CodeGroup codeGroup; // 코드 그룹 ID (FK)

    @Column(nullable = false)
    private Integer value; //  100, 101, 102

    @Column(length = 100, nullable = false)
    private String displayName; // 화면 표기명: "사용자", "관리자", "활성", "비활성"

    @Column
    private String description; // 설명, 툴팁

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
    private Boolean isActive = true; // 사용 여부

    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer sortOrder; // 정렬 순서

    @Column
    private LocalDateTime validFrom; // 유효 시작

    @Column
    private LocalDateTime validTo; // 유효 종료

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성 시각

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 시각

    /**
     * Business Methods
     */
    // 활성 상태 변경
    public void updateActiveStatus(Boolean isActive) {
        this.isActive = isActive;
    }

    // 코드명 변경
    public void updateName(String codeName) {
        this.displayName = codeName;
    }

    // 설명 변경
    public void updateDescription(String description) {
        this.description = description;
    }

    // 정렬 순서 변경
    public void changeSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * User 엔티티 호환성을 위한 편의 메서드들
     */
    // 코드가 유효한지 확인 (유효 기간 + 활성 상태)
    public boolean isValidNow() {
        if (!this.isActive) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean withinValidPeriod = (validFrom == null || !now.isBefore(validFrom)) &&
                (validTo == null || !now.isAfter(validTo));
        return withinValidPeriod;
    }

}