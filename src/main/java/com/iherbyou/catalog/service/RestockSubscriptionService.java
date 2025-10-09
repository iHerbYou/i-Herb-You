package com.iherbyou.catalog.service;

import com.iherbyou.catalog.dto.RestockSubscriptionDto;
import com.iherbyou.catalog.dto.RestockSubscriptionResponse;
import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.RestockSubscription;
import com.iherbyou.catalog.repository.ProductVariantRepository;
import com.iherbyou.catalog.repository.RestockSubscriptionRepository;
import com.iherbyou.exception.catalog.DuplicateSubscriptionException;
import com.iherbyou.exception.catalog.ProductNotFoundException;
import com.iherbyou.exception.catalog.RestockSubscriptionNotFoundException;
import com.iherbyou.exception.catalog.SubscriptionNotFoundException;
import com.iherbyou.exception.user.UserNotFoundException;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import com.iherbyou.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RestockSubscriptionService {

    private final UserRepository userRepository;
    private final ProductVariantRepository variantRepository;
    private final RestockSubscriptionRepository restockSubscriptionRepository;
    private final EmailService emailService;

    // 재입고 알림 목록 조회
    @Transactional(readOnly = true)
    public List<RestockSubscriptionResponse> getAllSubscriptions(Boolean isActive) {
        List<RestockSubscription> list;

        if (isActive == null) {
            // 전체 조회
            list = restockSubscriptionRepository.findAll();
        } else {
            // 활성화 여부로 필터링
            list = restockSubscriptionRepository.findByIsActive(isActive);
        }

        return list.stream()
                .map(RestockSubscriptionResponse::from)
                .collect(Collectors.toList());
    }

    // 재입고 알림 발송
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
        Optional<RestockSubscription> existingSubscription = restockSubscriptionRepository.findByUserAndProductVariant(user, variant);

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

        RestockSubscription saved = restockSubscriptionRepository.save(subscription);
        log.info("새로운 구독 생성 완료 - subscriptionId: {}", saved.getId());

        return RestockSubscriptionDto.from(saved);
    }

    @Transactional
    public void unsubscribe(Long userId, Long subscriptionId) {
        RestockSubscription subscription = restockSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        if (!subscription.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 구독만 해지할 수 있습니다.");
        }

        subscription.deactivate();
    }

    // 재입고 알림 발송
    @Transactional
    public void notifyRestock(Long id) {
        // 알림 신청 정보 조회
        RestockSubscription subscription = restockSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RestockSubscriptionNotFoundException(id));

        // 이미 비활성화된 구독(발송 완료)인지 확인
        if (Boolean.FALSE.equals(subscription.getIsActive())) {
            throw new IllegalStateException("This subscription is already inactive (notification sent).");
        }

        // 이메일 / 사용자 정보 가져오기
        String userEmail = subscription.getUser().getEmail();
        String productName = subscription.getProductVariant().getProduct().getName();
        String variantName = subscription.getProductVariant().getVariantName();

        // 실제 알림 발송 로직
        try {
            emailService.sendRestockNotification(userEmail, productName, variantName);
            log.info("재입고 알림 이메일 발송 성공: {} - [{} - {}]", userEmail, productName, variantName);
        } catch (Exception e) {
            log.error("재입고 알림 이메일 발송 실패: {} - [{} - {}], error: {}",
                    userEmail, productName, variantName, e.getMessage(), e);
            // 이메일 발송 실패해도 상태는 변경 (중복 발송 방지)
        }

        // 상태 변경 및 발송 시각 갱신
        subscription.deactivate();
        subscription.markAsNotified();
    }

}