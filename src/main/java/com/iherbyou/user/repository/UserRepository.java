package com.iherbyou.user.repository;

import com.iherbyou.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // email로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 중복 이메일 확인
    boolean existsByEmail(String email);

    // 핸드폰 번호로 사용자 찾기
    Optional<User> findByPhoneNumber(String phoneNumber);

    // 중복 핸드폰 번호 확인
    boolean existsByPhoneNumber(String phoneNumber);

    // ACTIVE(활성) 사용자만 조회 - displayName이 'ACTIVE'인 상태코드를 가진 사용자들
    @Query("SELECT u FROM User u WHERE u.statusCode.displayName = 'ACTIVE'")
    List<User> findActiveUsers();

    // 특정 권한 사용자들 조회
    @Query("SELECT u FROM User u WHERE u.roleCode.value = :roleValue")
    List<User> findByUserRoleCode(@Param("roleValue") Integer roleValue);

    // 활성 사용자 중 email로 찾기 - statusCode를 확인
    @Query("SELECT u FROM User u WHERE u.statusCode.displayName = 'ACTIVE' AND u.email = :email")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    // 관리자 목록 조회 - roleCode의 displayName에 'ADMIN'이 포함된 활성 사용자들
    @Query("SELECT u FROM User u WHERE u.roleCode.displayName LIKE '%ADMIN%' AND u.statusCode.displayName = 'ACTIVE'")
    List<User> findActiveAdmins();

    // 이름으로 사용자 검색 (부분 일치)
    List<User> findByNameContainingIgnoreCase(String name);

    // email, 전화번호로 사용자 찾기 (비밀번호 찾기 등에 사용)
    Optional<User> findByEmailAndPhoneNumber(String email, String phoneNumber);
}