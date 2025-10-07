package com.iherbyou.user.repository;

import com.iherbyou.user.entity.PasswordResetToken;
import com.iherbyou.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // 토큰 문자열로 조회
    Optional<PasswordResetToken> findByToken(String token);

    // 사용자의 모든 토큰 삭제 (재발송 시)
    void deleteByUser(User user);

}
