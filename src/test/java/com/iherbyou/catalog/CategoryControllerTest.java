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
public class CategoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test   // pass
    void 전체_카테고리_조회() throws Exception {
        mockMvc.perform(get("/catalog/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test   // pass
    void 카테고리_트리_조회() throws Exception {
        mockMvc.perform(get("/catalog/categories/tree"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test   // pass
    void 하위_카테고리_조회() throws Exception {
        mockMvc.perform(get("/catalog/categories/{id}/subcategories", 1L)) // 1번 카테고리의 하위 카테고리
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

}
