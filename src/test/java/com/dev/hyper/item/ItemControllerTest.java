package com.dev.hyper.item;

import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.item.request.CreateItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{productId}/items", 1L)
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
            mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{productId}/items", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andDo(print());
        }
    }
}