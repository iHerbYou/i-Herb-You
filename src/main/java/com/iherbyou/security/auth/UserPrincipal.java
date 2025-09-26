package com.iherbyou.security.auth;

import com.iherbyou.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;

    // User 엔티티로부터 UserPrincipal 생성
    public static UserPrincipal create(User user) {
        // 사용자 권한을 Role Code에서 가져오기
        String roleName = user.getRoleName(); // "USER" 또는 "ADMIN"
        Collection<GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return email; // 이메일을 username으로 사용
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // User 엔티티의 isActive()를 활용할 수도 있음
    }

}