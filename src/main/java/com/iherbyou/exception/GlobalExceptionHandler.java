package com.iherbyou.exception;

import com.iherbyou.exception.catalog.*;
import com.iherbyou.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 공통 JSON 응답 생성
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    // ===================== 회원가입 / 로그인 관련 예외 =====================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DuplicatePhoneNumberException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatePhone(DuplicatePhoneNumberException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<Map<String, Object>> handleInactiveUser(InactiveUserException e) {
        return buildResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPassword(InvalidPasswordException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    // ===================== 카테고리 / 상품 관련 예외 =====================

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("code", "CATEGORY_NOT_FOUND", "error", ex.getMessage()));
    }

    @ExceptionHandler(CategoryUnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(CategoryUnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("code", "UNAUTHORIZED", "error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidParentIdException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidParentId(InvalidParentIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("code", "INVALID_PARENT_ID", "error", ex.getMessage()));
    }

    @ExceptionHandler(CategoryForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(CategoryForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("code", "FORBIDDEN", "error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidParameter(InvalidParameterException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "INVALID_PARAMETER");
        body.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", "NOT_FOUND");
        errorResponse.put("error", "존재하지 않는 상품입니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    // ===================== 그 외 모든 예외 =====================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleServerError(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생: " + e.getMessage());
    }

}
