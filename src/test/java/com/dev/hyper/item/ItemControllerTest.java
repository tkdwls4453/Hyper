package com.dev.hyper.item;

import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.item.request.CreateItemRequest;
import com.dev.hyper.item.request.UpdateItemRequest;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("아이템 생성 테스트")
    class createItem{
        @Test
        @WithMockCustomUser
        @DisplayName("색상 정보없이 아이템을 생성시 400 에러를 반환한다.")
        void test1() throws Exception {
            // Given
            CreateItemRequest request = CreateItemRequest.builder()
//                    .color("BLACK")
                    .size("FREE")
                    .stock(10)
                    .build();

            // Expected
            mockMvc.perform(post("/api/products/{productId}/items", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("색상 정보는 필수입니다."))
                    .andDo(print());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("정상적으로 아이템 생성시 200 OK 를 반환한다.")
        void test1000() throws Exception {
            // Given
            CreateItemRequest request = CreateItemRequest.builder()
                    .color("BLACK")
                    .size("FREE")
                    .stock(10)
                    .build();

            // Expected
            mockMvc.perform(post("/api/products/{productId}/items", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("제품 아이디로 아이템 리스트 조회")
    class getItems{
        @Test
        @WithMockCustomUser
        @DisplayName("제품 아이디로 아이템을 조회하면 아이템 리스트를 반환한다.")
        void test1() throws Exception {
            // Given
            Long productId = 1L;

            // Expected
            mockMvc.perform(get("/api/products/{productId}/items", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andExpect(jsonPath("$.data").exists())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("아이템 정보 수정 테스트")
    class updateItem{
        @Test
        @WithMockCustomUser
        @DisplayName("아이템 정보 수정을 요청하면, 수정하고 200 OK 를 반환한다.")
        void test1000() throws Exception {
            // Given
            UpdateItemRequest request = UpdateItemRequest.builder()
                    .color("update color")
                    .size("update size")
                    .stock(2)
                    .build();


            // Expected
            mockMvc.perform(patch("/api/products/items/{itemId}", 1L)
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