package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReviewReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 신고 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_user_id", nullable = false) // 신고자 id
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false) // 리뷰 id
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_code_id", nullable = false)   // 신고사유 코드 id
    private Code reasonCode;

    @Column
    private String reasonText; // 신고사유 상세

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code_id", nullable = false)   // 처리상태 코드 id
    private Code statusCode;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;    // 신고 일시

    @Column
    private LocalDateTime reviewedAt;

}
