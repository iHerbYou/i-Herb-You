package com.iherbyou.catalog.controller;

import com.iherbyou.catalog.dto.ProductListDto;
import com.iherbyou.catalog.dto.SuggestionResponse;
import com.iherbyou.catalog.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/catalog/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<Page<ProductListDto>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sales") String sort) {

        // URL 디코딩 처리
        String decodedQuery = query;
        try {
            // 이미 인코딩된 문자열인지 확인 후 디코딩
            if (query.contains("%")) {
                decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            // 디코딩 실패시 원본 사용
            decodedQuery = query;
        }

        log.info("Search request - Original query: '{}', Decoded query: '{}', page: {}, size: {}, sort: {}",
                query, decodedQuery, page, size, sort);

        Page<ProductListDto> result = searchService.search(decodedQuery, page - 1, size, sort);

        log.info("Search result - Total elements: {}, Total pages: {}, Current page: {}",
                result.getTotalElements(), result.getTotalPages(), result.getNumber());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<SuggestionResponse> getSuggestions(@RequestParam String query) {

        // URL 디코딩 처리
        String decodedQuery = query;
        try {
            if (query.contains("%")) {
                decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.warn("Failed to decode suggestion query: {}", query, e);
            decodedQuery = query;
        }

        log.info("Suggestion request - Original query: '{}', Decoded query: '{}'", query, decodedQuery);

        SuggestionResponse result = searchService.getSuggestions(decodedQuery);

        log.info("Suggestion result - Categories: {}, Products: {}, Redirect: {}",
                result.getCategories().size(), result.getProducts().size(), result.getRedirectCategoryId());

        return ResponseEntity.ok(result);
    }

}