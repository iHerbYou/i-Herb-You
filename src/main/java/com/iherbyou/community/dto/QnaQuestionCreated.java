package com.iherbyou.community.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


//  리뷰 반영: POST 생성 응답을 간결화(answers 제외) + 메시지 포함
// 생성 결과를 확인하는 최소 정보 + 안내 메시지를 제공

@JsonInclude(JsonInclude.Include.NON_NULL)
public record QnaQuestionCreated(
        Long id,          // 생성된 질문 ID
        Long productId,   // 상품 ID
        Long userId,      // 작성자 ID
        String title,     // 질문 제목
        String content,   // 질문 내용
        String createdAt, // ISO 포맷 생성일
        String message    // "질문이 등록되었습니다."
) {}
