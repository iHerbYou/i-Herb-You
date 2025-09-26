package com.iherbyou.community.dto;
//QnA 답변 응담 DTO
public record QnaAnswerProduct(
        Long id, // 답변 ID
        Long userId, // 답변 작성자 ID
        String content, // 답변 내용
        String createdAt // 답변 작성일시
) {}
