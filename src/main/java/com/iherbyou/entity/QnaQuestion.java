package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class QnaQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 질문 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;    // 상품 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 질문 작성자 id
    private User user;

    @Column(nullable = false)
    private String title;   // 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;    // 작성일

    @Column(nullable = false, columnDefinition = "INT DEFAULT 101")
    private Integer statusCodeId;   // 상태 코드

    // 1:N (질문 한 개에 여러 답변)
//    @OneToMany(mappedBy = "QnaQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<QnaAnswer> answers = new ArrayList<>();

}
