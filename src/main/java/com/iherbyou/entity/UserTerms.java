package com.iherbyou.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "terms_id"})})
public class UserTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    @Column(name = "is_terms_agreed", nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isTermsAgreed = true;  // 동의 여부 (1 = 동의, 0 = 비동의)

    @Column(name = "agreed_at", nullable = false, updatable = true,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime agreedAt;  // 동의 시각
}
