package com.iherbyou.community;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class ReviewReport { // User 와 Review의 중간 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 신고자 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review; // 리뷰 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_code_id", nullable = false)
    private Code reasonCode; // 신고사유 코드 id

    @Column(length = 1000)
    private String reasonText; // 신고사유 상세

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code_id", nullable = false)
    private Code statusCode; // 처리상태 코드 id (접수, 검토중, 승인, 기각)

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 신고 일시

    @Column
    private LocalDateTime reviewedAt;

}
