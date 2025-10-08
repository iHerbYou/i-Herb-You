package com.iherbyou.exception;

import com.iherbyou.exception.banner.*;
import com.iherbyou.exception.catalog.*;
import com.iherbyou.exception.email.AlreadyVerifiedTokenException;
import com.iherbyou.exception.email.ExpiredEmailTokenException;
import com.iherbyou.exception.email.InvalidEmailTokenException;
import com.iherbyou.exception.password.ExpiredPasswordResetTokenException;
import com.iherbyou.exception.password.InvalidPasswordResetTokenException;
import com.iherbyou.exception.password.UsedPasswordResetTokenException;
import com.iherbyou.exception.user.*;
import com.iherbyou.exception.wishlist.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // ===================== 이메일 인증 관련 예외 =====================

    @ExceptionHandler(InvalidEmailTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEmailToken(InvalidEmailTokenException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ExpiredEmailTokenException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredEmailToken(ExpiredEmailTokenException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AlreadyVerifiedTokenException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyVerifiedEmail(AlreadyVerifiedTokenException e) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    // ===================== 비밀번호 재설정 관련 예외 =====================

    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPasswordResetToken(InvalidPasswordResetTokenException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ExpiredPasswordResetTokenException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredPasswordResetToken(ExpiredPasswordResetTokenException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UsedPasswordResetTokenException.class)
    public ResponseEntity<Map<String, Object>> handleUsedPasswordResetToken(UsedPasswordResetTokenException e) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage());
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

    // ===================== 위시리스트 관련 예외 =====================

    @ExceptionHandler(WishlistException.class)
    public ResponseEntity<Map<String, Object>> handleWishlistException(WishlistException e) {
        HttpStatus status = determineHttpStatus(e.getCode());

        return ResponseEntity.status(status)
                .body(Map.of("code", e.getCode(), "error", e.getMessage()));
    }

    /**
     * 위시리스트 예외 코드에 따른 HTTP 상태 코드 결정
     */
    private HttpStatus determineHttpStatus(String code) {
        return switch (code) {
            case "WISHLIST_NOT_FOUND", "WISHLIST_ITEM_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "WISHLIST_FULL" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    // ===================== 배너 관련 예외 =====================

    /**
     * 배너를 찾을 수 없을 때
     */
    @ExceptionHandler(BannerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBannerNotFound(BannerNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * 배너 정렬 순서 중복
     */
    @ExceptionHandler(DuplicateSortOrderException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateSortOrder(DuplicateSortOrderException e) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Validation 예외 처리 (@Valid 검증 실패 시)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ===================== 그 외 모든 예외 =====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleServerError(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생: " + e.getMessage());
    }

}