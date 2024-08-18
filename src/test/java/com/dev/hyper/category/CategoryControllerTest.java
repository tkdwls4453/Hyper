package com.dev.hyper.category;

import com.dev.hyper.category.request.CreateCategoryRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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
            mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("카테고리 이름은 필수입니다."));
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
            mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("FORBBIDEN"));

        }
    }
}