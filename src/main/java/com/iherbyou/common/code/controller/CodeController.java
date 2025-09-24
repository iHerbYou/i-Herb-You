package com.iherbyou.common.code.controller;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.entity.CodeGroup;
import com.iherbyou.common.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/codes")
@RestController
public class CodeController {

    private final CodeService codeService;

    // 모든 활성 코드 그룹 조회
    @GetMapping("/groups")
    public ResponseEntity<List<CodeGroup>> getAllCodeGroups() {
        log.info("모든 ACTIVE 코드 그룹 조회 요청");
        List<CodeGroup> codeGroups = codeService.getAllActiveCodeGroups();
        return ResponseEntity.ok(codeGroups);
    }

    // 특정 코드 그룹의 코드 목록 조회
    @GetMapping("/group/{groupValue}")
    public ResponseEntity<List<Code>> getCodesByGroup(@PathVariable Integer groupValue) {
        log.info("코드 그룹 {} 의 코드 목록 조회 요청", groupValue);
        List<Code> codes = codeService.getActiveCodesByGroup(groupValue);
        return ResponseEntity.ok(codes);
    }

    // 특정 코드 그룹을 Map 형태로 조회 (코드값 -> 코드명)
    @GetMapping("/group/{groupValue}/map")
    public ResponseEntity<Map<Integer, String>> getCodeMapByGroup(@PathVariable Integer groupValue) {
        log.info("코드 그룹 {} 의 코드 맵 조회 요청", groupValue);
        Map<Integer, String> codeMap = codeService.getCodeMap(groupValue);
        return ResponseEntity.ok(codeMap);
    }

    // 특정 코드 조회
    @GetMapping("/group/{groupValue}/code/{codeValue}")
    public ResponseEntity<Code> getCode(@PathVariable Integer groupValue, @PathVariable Integer codeValue) {
        log.info("코드 조회 요청: 그룹 {} - 코드 {}", groupValue, codeValue);
        Code code = codeService.getCode(groupValue, codeValue);

        if (code == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(code);
    }

    // 특정 코드 유효성 검증
    @GetMapping("/group/{groupValue}/code/{codeValue}/validate")
    public ResponseEntity<Map<String, Object>> validateCode(
            @PathVariable Integer groupValue,
            @PathVariable Integer codeValue) {

        log.info("코드 유효성 검증 요청: 그룹 {} - 코드 {}", groupValue, codeValue);

        boolean isValid = codeService.isValidCode(groupValue, codeValue);
        String codeName = isValid ? codeService.getCodeName(groupValue, codeValue) : null;

        Map<String, Object> result = Map.of(
                "valid", isValid,
                "codeName", codeName != null ? codeName : "유효하지 않은 코드",
                "groupValue", groupValue,
                "codeValue", codeValue
        );

        return ResponseEntity.ok(result);
    }

    // 사용자 권한 코드 목록 조회
    @GetMapping("/user-roles")
    public ResponseEntity<List<Code>> getUserRoleCodes() {
        log.info("사용자 권한 코드 조회 요청");
        List<Code> userRoles = codeService.getUserRoleCodes();
        return ResponseEntity.ok(userRoles);
    }

    // 사용자 상태 코드 목록 조회
    @GetMapping("/user-statuses")
    public ResponseEntity<List<Code>> getUserStatusCodes() {
        log.info("사용자 상태 코드 조회 요청");
        List<Code> userStatuses = codeService.getUserStatusCodes();
        return ResponseEntity.ok(userStatuses);
    }

    // 주문 상태 코드 목록 조회
    @GetMapping("/order-statuses")
    public ResponseEntity<List<Code>> getOrderStatusCodes() {
        log.info("주문 상태 코드 조회 요청");
        List<Code> orderStatuses = codeService.getOrderStatusCodes();
        return ResponseEntity.ok(orderStatuses);
    }

    // 배송 상태 코드 목록 조회
    @GetMapping("/delivery-statuses")
    public ResponseEntity<List<Code>> getDeliveryStatusCodes() {
        log.info("배송 상태 코드 조회 요청");
        List<Code> deliveryStatuses = codeService.getDeliveryStatusCodes();
        return ResponseEntity.ok(deliveryStatuses);
    }

    // 결제 방법 코드 목록 조회
    @GetMapping("/payment-methods")
    public ResponseEntity<List<Code>> getPaymentMethodCodes() {
        log.info("결제 방법 코드 조회 요청");
        List<Code> paymentMethods = codeService.getPaymentMethodCodes();
        return ResponseEntity.ok(paymentMethods);
    }

    // 로그인 제공자 코드 목록 조회
    @GetMapping("/login-providers")
    public ResponseEntity<List<Code>> getLoginProviderCodes() {
        log.info("로그인 제공자 코드 조회 요청");
        List<Code> loginProviders = codeService.getLoginProviderCodes();
        return ResponseEntity.ok(loginProviders);
    }

}
