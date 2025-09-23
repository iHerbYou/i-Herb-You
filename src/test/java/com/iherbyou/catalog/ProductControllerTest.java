package com.iherbyou.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test   // pass
    void 상품_목록_조회() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품_목록_정렬_판매량() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("sort", "sales")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품_목록_정렬_평점() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("sort", "rating")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품_목록_정렬_리뷰수() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("sort", "reviews")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품목록조회_가격_오름차순() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("sort", "price")
                        .param("direction", "asc")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // 첫 번째 상품에 minPrice 필드가 있는지 확인
                .andExpect(jsonPath("$.content[0].minPrice").exists());
    }

    @Test
    void 상품목록조회_가격_내림차순() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("sort", "price")
                        .param("direction", "desc")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // 첫 번째 상품에 minPrice 필드가 있는지 확인
                .andExpect(jsonPath("$.content[0].minPrice").exists());
    }

    @Test   // pass
    void 상품_목록_품절제외() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("excludeSoldOut", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품_목록_금액범위() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("minPrice", "10000")
                        .param("maxPrice", "50000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품_목록_복합_필터() throws Exception {
        mockMvc.perform(get("/catalog/products")
                        .param("excludeSoldOut", "true")
                        .param("minPrice", "5000")
                        .param("maxPrice", "100000")
                        .param("sort", "price")
                        .param("direction", "asc")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void 상품_상세_조회() throws Exception {
        // given: DB에 id=1 상품 더미데이터 있다고 가정
        Long productId = 1L;

        mockMvc.perform(get("/catalog/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // 기본 필드 확인
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.brand.name").exists())

                // 카테고리 (breadcrumb)
                .andExpect(jsonPath("$.breadcrumbs").isArray())

                // 집계 데이터
                .andExpect(jsonPath("$.avgRating").exists())
                .andExpect(jsonPath("$.reviewCount").exists())
                .andExpect(jsonPath("$.sales").exists())

                // 옵션(variants)
                .andExpect(jsonPath("$.variants").isArray())
                .andExpect(jsonPath("$.variants[0].salePrice").exists())

                // 이미지
                .andExpect(jsonPath("$.images").isArray())

                // 상품 상세 info
                .andExpect(jsonPath("$.info.description").exists())
                .andExpect(jsonPath("$.info.ingredients").exists());
    }

}
