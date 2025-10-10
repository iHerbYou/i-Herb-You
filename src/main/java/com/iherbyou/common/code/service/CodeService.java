package com.iherbyou.common.code.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.entity.CodeGroup;
import com.iherbyou.common.code.repository.CodeRepository;
import com.iherbyou.common.code.repository.CodeGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

    private final CodeRepository codeRepository;
    private final CodeGroupRepository codeGroupRepository;

    /**
     * 특정 코드 그룹의 활성 코드들 조회 (캐시 적용)
     */
    @Cacheable(value = "codes", key = "#groupValue")
    public List<Code> getActiveCodesByGroup(Integer groupValue) {
        CodeGroup codeGroup = codeGroupRepository.findByValueAndIsActiveTrue(groupValue).orElse(null);
        if (codeGroup == null) {
            log.warn("활성 코드 그룹을 찾을 수 없습니다: {}", groupValue);
            return List.of();
        }
        return codeRepository.findByCodeGroupAndIsActiveTrueOrderBySortOrder(codeGroup);
    }

    /**
     * 코드 그룹 value와 코드 value로 코드명 조회
     */
    @Cacheable(value = "codeName", key = "#groupValue + '_' + #codeValue")
    public String getCodeName(Integer groupValue, Integer codeValue) {
        return codeRepository.findActiveByGroupValueAndCodeValue(groupValue, codeValue)
                .map(Code::getDisplayName)
                .orElse("알 수 없음");
    }

    /**
     * 코드 그룹 value와 코드 value로 Code 엔티티 조회
     */
    public Code getCode(Integer groupValue, Integer codeValue) {
        return codeRepository.findActiveByGroupValueAndCodeValue(groupValue, codeValue)
                .orElse(null);
    }

    /**
     * ID로 Code 엔티티 조회 (활성 + 유효 기간 체크)
     */
    public Code getCodeById(Long codeId) {
        return codeRepository.findActiveById(codeId)
                .orElse(null);
    }

    /**
     * 코드 유효성 검증
     */
    public boolean isValidCode(Integer groupValue, Integer codeValue) {
        return codeRepository.existsActiveByGroupValueAndCodeValue(groupValue, codeValue);
    }

    /**
     * 특정 코드 그룹을 Map으로 반환 (codeValue -> displayName)
     */
    @Cacheable(value = "codeMap", key = "#groupValue")
    public Map<Integer, String> getCodeMap(Integer groupValue) {
        return getActiveCodesByGroup(groupValue).stream()
                .collect(Collectors.toMap(Code::getValue, Code::getDisplayName));
    }

    /**
     * 코드 그룹 조회
     */
    public CodeGroup getCodeGroup(Integer groupValue) {
        return codeGroupRepository.findByValueAndIsActiveTrue(groupValue).orElse(null);
    }

    /**
     * 모든 활성 코드 그룹 조회
     */
    public List<CodeGroup> getAllActiveCodeGroups() {
        return codeGroupRepository.findByIsActiveTrueOrderBySortOrder();
    }

    /**
     * 사용자 권한 관련 편의 메서드들 (UserService에서 사용)
     * 그룹 70: USER_ROLE
     * 그룹 71: USER_STATUS
     */
    public String getUserRoleName(Integer roleValue) {
        return getCodeName(70, roleValue);
    }

    public String getUserStatusName(Integer statusValue) {
        return getCodeName(71, statusValue);
    }

    public boolean isValidUserRole(Integer roleValue) {
        return isValidCode(70, roleValue);
    }

    public boolean isValidUserStatus(Integer statusValue) {
        return isValidCode(71, statusValue);
    }

    /**
     * 사용자 권한/상태 코드 목록
     */
    public List<Code> getUserRoleCodes() {
        return getActiveCodesByGroup(70);
    }

    public List<Code> getUserStatusCodes() {
        return getActiveCodesByGroup(71);
    }

    /**
     * 특정 권한/상태 확인 편의 메서드들
     */
    public boolean isAdminRole(Integer roleValue) {
        Code code = getCode(70, roleValue);
        return code != null && code.getDisplayName().contains("ADMIN");
    }

    public boolean isActiveStatus(Integer statusValue) {
        Code code = getCode(71, statusValue);
        return code != null && "ACTIVE".equals(code.getDisplayName());
    }

    /**
     * 기본 코드 조회 편의 메서드들 (UserService에서 사용)
     *
     * 코드 체계:
     * - 그룹 70 (USER_ROLE): 701=USER, 702=ADMIN_BUSINESS, ...
     * - 그룹 71 (USER_STATUS): 712=ACTIVE, 713=SUSPENDED, 714=DORMANT, 711=DELETED
     */
    public Code getDefaultUserRole() {
        return getCode(70, 701); // 70번 그룹, 701번 코드 = USER 권한
    }

    public Code getDefaultActiveStatus() {
        return getCode(71, 712); // 71번 그룹, 712번 코드 = ACTIVE 상태
    }

    /**
     * 기타 도메인별 편의 메서드들
     */
    public List<Code> getOrderStatusCodes() {
        return getActiveCodesByGroup(30); // ORDER_STATUS
    }

    public List<Code> getDeliveryStatusCodes() {
        return getActiveCodesByGroup(20); // DELIVERY_STATUS
    }

    public List<Code> getPaymentMethodCodes() {
        return getActiveCodesByGroup(41); // PAYMENT_METHOD
    }

    public List<Code> getLoginProviderCodes() {
        return getActiveCodesByGroup(80); // LOGIN_PROVIDER
    }
}