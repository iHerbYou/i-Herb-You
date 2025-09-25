package com.iherbyou.community.dto;
//QnA 답변 생성 요청 DTO
public record QnaAnswerCreateRequest(
        Long questionId, // 답변 대상 질문 ID
        String content // 답변 내용
) {}
