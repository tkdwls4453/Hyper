package com.dev.hyper.category;

import com.dev.hyper.category.controller.CategoryController;
import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.category.request.UpdateCategoryRequest;
import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.common.config.SecurityConfig;
import com.dev.hyper.common.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtUtil jwtUtil;
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
        @WithMockCustomUser(role = "SELLER")
        @DisplayName("어드민이 아닌 유저가 카테고리 생성시, 403 에러를 반환한다.")
        void test2() throws Exception {
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
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockCustomUser(role = "ADMIN")
        @DisplayName("카테고리 생성시, 200 OK 를 반환한다.")
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
    class updateCategory {

        @Test
        @DisplayName("카테고리를 성공적으로 수정하면 200 OK를 반환한다.")
        void test1000() throws Exception {
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

    @Nested
    @WithMockCustomUser(role = "ADMIN")
    class deleteCategory{
        @Test
        @DisplayName("카테고리를 성공적으로 삭제하면 200 OK 를 반환한다.")
        void test1000() throws Exception{
            // Given
            Long id = 1L;

            // Expected
            mockMvc.perform(delete("/api/categories/{categoryId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"));
        }
    }
}