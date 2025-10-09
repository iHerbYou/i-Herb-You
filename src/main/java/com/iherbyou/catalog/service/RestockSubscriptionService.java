package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.RestockSubscriptionDto;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.RestockSubscription;
import com.iherbyou.catalog.repository.ProductVariantRepository;
import com.iherbyou.catalog.repository.RestockSubscriptionRepository;
import com.iherbyou.exception.catalog.DuplicateSubscriptionException;
import com.iherbyou.exception.catalog.ProductNotFoundException;
import com.iherbyou.exception.catalog.SubscriptionNotFoundException;
import com.iherbyou.exception.user.UserNotFoundException;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RestockSubscriptionService {

    private final RestockSubscriptionRepository repository;
    private final UserRepository userRepository;
    private final ProductVariantRepository variantRepository;

    public RestockSubscriptionDto subscribe(Long userId, Long variantId) {
        log.info("재입고알림 구독 시작 - userId: {}, variantId: {}", userId, variantId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다. userId: {}", userId);
                    return new UserNotFoundException("사용자를 찾을 수 없습니다. id=" + userId);
                });
        log.info("사용자 조회 성공 - userId: {}, email: {}", userId, user.getEmail());

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> {
                    log.error("상품 옵션을 찾을 수 없습니다. variantId: {}", variantId);
                    return new ProductNotFoundException(variantId);
                });
        log.info("상품 옵션 조회 성공 - variantId: {}, variantName: {}", variantId, variant.getVariantName());

        // 중복 등록 방지
        log.info("기존 구독 확인 중 - userId: {}, variantId: {}", userId, variantId);
        Optional<RestockSubscription> existingSubscription = repository.findByUserAndProductVariant(user, variant);

        if (existingSubscription.isPresent()) {
            RestockSubscription rs = existingSubscription.get();
            log.info("기존 구독 발견 - subscriptionId: {}, isActive: {}", rs.getId(), rs.getIsActive());

            if (Boolean.TRUE.equals(rs.getIsActive())) {
                log.warn("이미 활성화된 구독이 존재합니다 - subscriptionId: {}", rs.getId());
                throw new DuplicateSubscriptionException();
            } else {
                log.info("비활성 구독을 활성화합니다 - subscriptionId: {}", rs.getId());
                rs.activate(); // 비활성 상태였다면 다시 활성화
                return RestockSubscriptionDto.from(rs);
            }
        }

        // 새로운 구독 생성
        log.info("새로운 구독 생성 중 - userId: {}, variantId: {}", userId, variantId);
        RestockSubscription subscription = RestockSubscription.builder()
                .user(user)
                .productVariant(variant)
                .isActive(true)
                .build();

        RestockSubscription saved = repository.save(subscription);
        log.info("새로운 구독 생성 완료 - subscriptionId: {}", saved.getId());

        return RestockSubscriptionDto.from(saved);
    }

    @Transactional
    public void unsubscribe(Long userId, Long subscriptionId) {
        RestockSubscription subscription = repository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        if (!subscription.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 구독만 해지할 수 있습니다.");
        }

        subscription.deactivate();
    }

}