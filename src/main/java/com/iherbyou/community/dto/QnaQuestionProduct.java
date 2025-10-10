package com.iherbyou.community.dto;

import java.util.List;
// QnA 질문 응답 DTO (질문 기본 정보 + 답변 리스트 + 답변 개수)
public record QnaQuestionProduct(
        Long id, // 질문 ID
        Long productId, // 상품 ID
        Long userId, // 질문 작성자 ID
        String userEmail,   // 질문 작성자 email
        String title, // 질문 제목
        String content, // 질문 내용
        String createdAt, // 작성일시
        List<QnaAnswerProduct> answers, // 답변 목록
        int answerCount
) {}
