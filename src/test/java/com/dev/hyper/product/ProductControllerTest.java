package com.dev.hyper.product;

import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.product.request.CreateProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("[컨트롤러] 제품 생성 테스트")
    @Nested
    class createProduct{
        final String name = "product name";
        final String description = "product description";

        @Test
        @WithMockCustomUser
        @DisplayName("제품 이름없이 제품 생성을 요청하면 400 에러를 반환합니다.")
        void test1() throws Exception {
            // Given
            CreateProductRequest request = CreateProductRequest.builder()
//                    .name(name)
                    .description(description)
                    .build();

            // Expected
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("제품 이름은 필수입니다."))
                    .andDo(print());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("정상적으로 제품 생성을 요청하면 200 OK 를 반환합니다.")
        void test1000() throws Exception {
            // Given
            CreateProductRequest request = CreateProductRequest.builder()
                    .name(name)
                    .description(description)
                    .build();

            // Expected
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andDo(print());
        }
    }

}