package com.iherbyou.security.auth;

import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 메서드 이름을 email로 하고싶은데, override 하는거라 username으로 함
        log.debug("Loading user by username(email): {}", username);

        // email을 username으로 사용하여 활성 사용자만 조회
        User user = userRepository.findActiveUserByEmail(username)
                .orElseThrow(() -> {
                    log.warn("User not found or inactive: {}", username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없거나 비활성 상태입니다: " + username);
                });

        log.debug("Successfully loaded user: {} with role: {}", user.getEmail(), user.getRoleName());
        return UserPrincipal.create(user);
    }

    // ID로 사용자 로드 (JWT에서 사용자 ID를 사용할 경우 - 선택적)
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return UserPrincipal.create(user);
    }

}