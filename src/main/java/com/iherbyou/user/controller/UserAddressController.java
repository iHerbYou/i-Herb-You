package com.iherbyou.user.controller;

import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.user.dto.UserAddressCreateRequestDto;
import com.iherbyou.user.dto.UserAddressResponseDto;
import com.iherbyou.user.dto.UserAddressUpdateRequestDto;
import com.iherbyou.user.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/addresses")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    // 내 배송지 목록 조회
    @GetMapping
    public List<UserAddressResponseDto> getAddresses(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = requirePrincipal(principal);
        return userAddressService.getAddresses(userId);
    }

    // 새 배송지 등록 (첫 주소 또는 isDefault=true면 기본 지정)
    @PostMapping
    public ResponseEntity<UserAddressResponseDto> createAddress(@AuthenticationPrincipal UserPrincipal principal,
                                                                @Valid @RequestBody UserAddressCreateRequestDto request) {
        Long userId = requirePrincipal(principal);
        UserAddressResponseDto response = userAddressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 배송지 정보 수정 + 기본 여부 변경 가능
    @PutMapping("/{addressId}")
    public UserAddressResponseDto updateAddress(@AuthenticationPrincipal UserPrincipal principal,
                                                @PathVariable Long addressId,
                                                @Valid @RequestBody UserAddressUpdateRequestDto request) {
        Long userId = requirePrincipal(principal);
        return userAddressService.updateAddress(userId, addressId, request);
    }

    // 배송지 삭제 (기본 삭제 시 다른 주소를 기본으로 승격)
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long addressId) {
        Long userId = requirePrincipal(principal);
        userAddressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    // 지정한 배송지를 기본 배송지로 전환
    @PostMapping("/{addressId}/default")
    public UserAddressResponseDto makeDefault(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long addressId) {
        Long userId = requirePrincipal(principal);
        return userAddressService.setDefaultAddress(userId, addressId);
    }

    private Long requirePrincipal(UserPrincipal principal) {
        if (principal == null || principal.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authentication required");
        }
        return principal.getId();
    }
}
