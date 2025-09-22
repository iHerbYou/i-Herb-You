package com.iherbyou.user.service;

import com.iherbyou.exception.user.*;
import com.iherbyou.user.dto.LoginRequestDto;
import com.iherbyou.user.dto.LoginResponseDto;
import com.iherbyou.user.dto.SignUpRequestDto;
import com.iherbyou.user.dto.SignUpResponseDto;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입 (SignUp)
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto request) {
        log.info("signUp requestDto: {}", request.getEmail());

        // 중복 email 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }

        // 전화번호 중복 확인 (전화번호가 있는 경우)
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()
                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicatePhoneNumberException("이미 사용 중인 전화번호입니다:" + request.getPhoneNumber());
        }

        // User 엔티티 생성 (TODO: roleCode, statusCode는 일단 null 처리 → 추후 기본값 세팅 로직 추가 가능)
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // TODO: 나중에 암호화
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        // 저장
        User savedUser = userRepository.save(user);

        // 반환
        return SignUpResponseDto.builder()
                .email(savedUser.getEmail())
                .message("회원가입이 완료되었습니다. 로그인 해주세요.")
                .build();
    }

    /**
     * 로그인 (Login)
     */
    public LoginResponseDto login(LoginRequestDto request) {

        // 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("가입되지 않은 회원입니다"));

        // Active 상태 확인
        if (!user.isActive()) {
            throw new InactiveUserException("비활성 사용자입니다");
        }

        // password 검증
        if (user.getPassword().equals(request.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다");
        }

        // 로그인 성공
        return LoginResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .message("로그인 성공")
                .build();
    }
}
