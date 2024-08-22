package com.dev.hyper.product;

import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.common.config.SecurityConfig;
import com.dev.hyper.common.util.JwtUtil;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.product.request.UpdateProductRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @DisplayName("제품 생성 테스트")
    @Nested
    class createProduct {
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
                    .price(4000)
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
        @DisplayName("제품의 가격 정보없이 제품을 생성시 400 에러를 반환한다.")
        void test2() throws Exception {
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
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("제품 가격은 필수입니다."))
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
                    .price(4000)
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

    @Nested
    @DisplayName("제품 수정 테스트")
    class updateProduct{
        final String name = "product name";
        final String description = "product description";
        @Test
        @WithMockCustomUser
        @DisplayName("제품 이름없이 제품 수정을 요청하면 400 에러를 반환합니다.")
        void test1() throws Exception {
            // Given
            UpdateProductRequest request = UpdateProductRequest.builder()
//                    .name(name)
                    .description(description)
                    .build();

            // Expected
            mockMvc.perform(patch("/api/products/1")
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
        @DisplayName("정상적으로 제품 수정을 요청하면 200 OK 를 반환합니다.")
        void test1000() throws Exception {
            // Given
            UpdateProductRequest request = UpdateProductRequest.builder()
                    .name(name)
                    .description(description)
                    .build();

            // Expected
            mockMvc.perform(patch("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("제품 삭제 테스트")
    class deleteProduct{
        @Test
        @WithMockCustomUser(role = "BUYER")
        @DisplayName("권한이 없는 유저가 제품 삭제시, 403 을 반환한다.")
        void test1() throws Exception {
            // Given

            // Expected
            mockMvc.perform(delete("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                    )
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }
        @Test
        @WithMockCustomUser
        @DisplayName("정상적으로 제품 수정을 요청하면 200 OK 를 반환합니다.")
        void test1000() throws Exception {
            // Given

            // Expected
            mockMvc.perform(delete("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("판매자의 제품 조회 테스트")
    class getProducts{
        @Test
        @WithMockCustomUser
        @DisplayName("정상적으로 제품을 조회하면 200 OK 를 반환한다.")
        void test1000() throws Exception {
            // Given

            // Expected
            mockMvc.perform(get("/api/products")
                            .param("size", "10")
                            .param("page", "0")
                            .param("sort", "latest")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
            ;
        }
    }
}