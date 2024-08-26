package com.dev.hyper.product.controller;

import com.dev.hyper.common.config.SecurityConfig;
import com.dev.hyper.common.util.JwtUtil;
import com.dev.hyper.product.ProductService;
import com.dev.hyper.product.request.FilterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ProductOpenController.class)
class ProductOpenControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("제품 검색 테스트")
    class searchProducts{
        @Test
        @DisplayName("검색 조건을 갖고 제품을 검색한다.")
        void test1() throws Exception {
            // Given
            FilterDto filterDto = FilterDto.builder()
                    .category("category")
                    .priceRange(List.of(3000, 7000))
                    .build();
            // Expected
            mockMvc.perform(get("/open-api/products/search")
                            .param("q", "search")
                            .param("sort", "expensive")
                            .param("filter", objectMapper.writeValueAsString(filterDto))
                            .param("size", "10")
                            .param("page", "0")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"));
        }
    }
}