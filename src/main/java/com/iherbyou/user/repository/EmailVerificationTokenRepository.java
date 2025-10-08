package com.iherbyou.user.repository;

import com.iherbyou.user.entity.EmailVerificationToken;
import com.iherbyou.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    // 토큰 문자열으로 조회
    Optional<EmailVerificationToken> findByToken(String token);

    // 사용자로 조회 (가장 최근 토큰)
    Optional<EmailVerificationToken> findByUserOrderByCreatedAtDesc(User user);

    // 사용자의 모든 토큰 삭제 (재발송 시)
    void deleteByUser(User user);
}
