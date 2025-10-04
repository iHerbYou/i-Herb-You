package com.iherbyou.user.entity;

import com.iherbyou.wishlist.entity.Wishlist;
import com.iherbyou.catalog.entity.RestockSubscription;
import com.iherbyou.common.code.entity.Code;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // 나중에 암호화 할 예정, TODO: 현재는 필수, 나중에 social login 하면 nullable 로 변경

    @Column(nullable = false, length = 30)
    private String name; // 실명

    @Column(length = 50, unique = true)
    private String phoneNumber;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code_id")
    private Code roleCode; // 회원 권한 (1: USER, 2: ADMIN)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code_id")
    private Code statusCode; // 회원 상태 (1: ACTIVE, 2: 비활성화, 3: 탈퇴)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>(); // 마이페이지에서 주소 목록 주기로했으니 추가함

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wishlist wishlist;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestockSubscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> coupons = new ArrayList<>(); // 쿠폰 목록 조회

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTerms> userTerms = new ArrayList<>(); // 약관 목록 조회


    /**
     * Business Methods
     */
    // 이름 변경
    public void changeName(String newName) {
        this.name = newName;
    }

    // 전화번호 변경
    public void changePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    // 비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 사용자 권한 변경
    public void changeUserRole(Code newUserRole) {
        this.roleCode = newUserRole;
    }

    // 사용자 상태 변경
    public void changeUserStatus(Code newUserStatus) {
        this.statusCode = newUserStatus;
    }

    // 활성 사용자인지 확인 TODO: Code 엔티티에 필요한 get Method 만들기
    public boolean isActive() {
        return this.statusCode != null && "ACTIVE".equals(this.statusCode.getDisplayName());
    }

    // 관리자인지 확인
    public boolean isAdmin() {
        return this.roleCode != null && "ADMIN".equals(this.roleCode.getDisplayName());
    }

    /**
     * 편의 메서드들 (필요시 추가 구현 가능)
     */
    public String getRoleName() {
        return this.roleCode != null ? this.roleCode.getDisplayName() : "알 수 없음";
    }

    public String getStatusName() {
        return this.statusCode != null ? this.statusCode.getDisplayName() : "알 수 없음";
    }

    public Integer getRoleCode() {
        return this.roleCode != null ? this.roleCode.getValue() : null;
    }

    public Integer getStatusCode() {
        return this.statusCode != null ? this.statusCode.getValue() : null;
    }

}
