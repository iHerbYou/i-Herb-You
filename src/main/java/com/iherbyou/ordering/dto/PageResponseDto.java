package com.iherbyou.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

// API 공통 페이지 응답 DTO. Spring Page 객체의 필드를 1-base 페이지 번호와 함께 래핑
@Getter
@AllArgsConstructor
public class PageResponseDto<T> {

    private final List<T> content;
    private final int page;            // 1-based 페이지 번호
    private final int size;            // 요청한 페이지 크기
    private final int count;           // 현재 페이지에 실린 건수
    private final long totalElements;  // 전체 건수
    private final int totalPages;      // 전체 페이지 수 (0이면 데이터 없음)
    private final boolean hasNext;     // 다음 페이지 존재 여부

    public static <T> PageResponseDto<T> from(Page<T> page, int requestedPage) {
        int oneBasedPage = Math.max(requestedPage, 1);
        return new PageResponseDto<>(
                page.getContent(),
                oneBasedPage,
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}

