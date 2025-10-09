package com.iherbyou.user.service;

import com.iherbyou.user.dto.UserAddressCreateRequestDto;
import com.iherbyou.user.dto.UserAddressResponseDto;
import com.iherbyou.user.dto.UserAddressUpdateRequestDto;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.entity.UserAddress;
import com.iherbyou.user.repository.UserAddressRepository;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Transactional(readOnly = true)
    public List<UserAddressResponseDto> getAddresses(Long userId) {
        return userAddressRepository.findByUser_IdOrderByIsDefaultDescIdDesc(userId).stream()
                .map(UserAddressResponseDto::from)
                .toList();
    }

    public UserAddressResponseDto createAddress(Long userId, UserAddressCreateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        List<UserAddress> existing = userAddressRepository.findByUser_IdOrderByIsDefaultDescIdDesc(userId);

        boolean hasDefault = existing.stream().anyMatch(UserAddress::isDefault);
        boolean requestedDefault = Boolean.TRUE.equals(request.getIsDefault());
        boolean shouldBeDefault = requestedDefault || !hasDefault;

        if (shouldBeDefault) {
            clearDefault(existing, null);
        }

        UserAddress address = UserAddress.builder()
                .user(user)
                .recipient(normalize(request.getRecipient(), "recipient"))
                .phone(normalize(request.getPhone(), "phone"))
                .zipcode(normalize(request.getZipcode(), "zipcode"))
                .address(normalize(request.getAddress(), "address"))
                .addressDetail(normalizeNullable(request.getAddressDetail()))
                .isDefault(shouldBeDefault)
                .build();

        UserAddress saved = userAddressRepository.save(address);
        return UserAddressResponseDto.from(saved);
    }

    public UserAddressResponseDto updateAddress(Long userId, Long addressId, UserAddressUpdateRequestDto request) {
        UserAddress address = userAddressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));

        List<UserAddress> addresses = userAddressRepository.findByUser_IdOrderByIsDefaultDescIdDesc(userId);
        boolean wasDefault = address.isDefault();
        Boolean requestDefault = request.getIsDefault();
        boolean onlyAddress = addresses.size() == 1;
        boolean newDefault = onlyAddress ? true : (requestDefault != null ? requestDefault : wasDefault);

        if (newDefault) {
            clearDefault(addresses, addressId);
        }

        address.update(
                normalize(request.getRecipient(), "recipient"),
                normalize(request.getPhone(), "phone"),
                normalize(request.getZipcode(), "zipcode"),
                normalize(request.getAddress(), "address"),
                normalizeNullable(request.getAddressDetail()),
                newDefault
        );

        if (wasDefault && !newDefault) {
            ensureDefaultExists(userId, addressId);
        }

        return UserAddressResponseDto.from(address);
    }

    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));

        boolean wasDefault = address.isDefault();
        userAddressRepository.delete(address);
        userAddressRepository.flush();

        if (wasDefault) {
            ensureDefaultExists(userId, null);
        }
    }

    public UserAddressResponseDto setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));

        List<UserAddress> addresses = userAddressRepository.findByUser_IdOrderByIsDefaultDescIdDesc(userId);
        clearDefault(addresses, null);
        address.changeDefault(true);

        return UserAddressResponseDto.from(address);
    }

    private void ensureDefaultExists(Long userId, Long excludeAddressId) {
        List<UserAddress> candidates = userAddressRepository.findByUser_IdOrderByIsDefaultDescIdDesc(userId);
        if (candidates.stream().anyMatch(UserAddress::isDefault)) {
            return;
        }
        if (candidates.isEmpty()) {
            return;
        }
        for (UserAddress candidate : candidates) {
            if (excludeAddressId != null && candidate.getId().equals(excludeAddressId)) {
                continue;
            }
            candidate.changeDefault(true);
            return;
        }
        // excludeAddressId was the only remaining address; keep it as default to maintain invariant
        candidates.get(0).changeDefault(true);
    }

    private String normalize(String value, String fieldName) {
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must not be null");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must not be blank");
        }
        return trimmed;
    }

    private String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void clearDefault(List<UserAddress> addresses, Long excludeId) {
        for (UserAddress address : addresses) {
            if (excludeId != null && address.getId().equals(excludeId)) {
                continue;
            }
            if (address.isDefault()) {
                address.changeDefault(false);
            }
        }
    }
}
