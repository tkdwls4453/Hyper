package com.dev.hyper.category.controller;

import com.dev.hyper.category.CategoryService;
import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.common.config.SecurityConfig;
import com.dev.hyper.common.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(CategoryOpenController.class)
class CategoryOpenControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtUtil jwtUtil;

    @Nested
    @DisplayName("카테고리 조회 테스트")
    class getCategories{

        @Test
        @DisplayName("모든 카테고리를 조회한다.")
        void test1000() throws Exception{
            // Given
            BDDMockito.given(categoryService.getCategories())
                    .willReturn(new ArrayList<>());

            // Expected
            mockMvc.perform(get("/open-api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andExpect(jsonPath("$.data").isArray());
        }
    }

}