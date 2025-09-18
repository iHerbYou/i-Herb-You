package com.iherbyou.user;

import com.iherbyou.common.Code;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class PointHistory { // 포인트 이력 관리

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int amount; // 포인트 증감 금액 +:적립, -:사용

    @Column(nullable = false)
    private int balanceAfter; // 트랜잭션 후 잔액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_type_code_id", nullable = false)
    private Code pointTypeCode; // EARN, USE, EXPIRE 같은 코드 참조 (적립, 사용, 만료)

    @Column
    private String reason; // 상세 설명 (Text) ex "이벤트 참여 보상", "1년 경과 만료"

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

}
