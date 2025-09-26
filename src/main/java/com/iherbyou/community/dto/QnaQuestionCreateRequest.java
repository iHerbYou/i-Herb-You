package com.iherbyou.community.dto;
//QnA 질문 생성 요청 DTO
public record QnaQuestionCreateRequest(
        Long productId, // 질문 대상 상품 ID
        String title, // 질문 제목
        String content // 질문 내용
) {}
