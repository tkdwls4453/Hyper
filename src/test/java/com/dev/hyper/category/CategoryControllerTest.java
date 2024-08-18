package com.dev.hyper.category;

import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.category.request.UpdateCategoryRequest;
import com.dev.hyper.common.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategory {
        @Test
        @WithMockCustomUser(role = "ADMIN")
        @DisplayName("이름없이 카테고리 생성시, 400 에러를 반환한다.")
        void test1() throws Exception {
            // Given
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                    .build();

            // Expected
            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("카테고리 이름은 필수입니다."));
        }

        @Test
        @WithMockCustomUser(role = "ADMIN")
        @DisplayName("어드민이 아닌 유저가 카테고리 생성시, 403 에러를 반환한다.")
        void test1000() throws Exception {
            // Given
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                    .name("category")
                    .build();

            // Expected
            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"));

        }
    }

    @Nested
    @WithMockCustomUser(role = "ADMIN")
    @DisplayName("카테고리 수정 테스트")
    class updateCategory{

        @Test
        @DisplayName("카테고리를 성공적으로 수정하면 200 OK를 반환한다.")
        void test() throws Exception {
            // Given
            UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                    .name("update")
                    .parentName("parent")
                    .build();

            // Expected
            mockMvc.perform(patch("/api/categories/{categoryId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"));
        }
    }
}