package com.dev.hyper.order.controller;

import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.common.config.SecurityConfig;
import com.dev.hyper.common.util.JwtUtil;
import com.dev.hyper.order.request.OrderItemInfo;
import com.dev.hyper.order.request.OrderRequest;
import com.dev.hyper.order.service.OrderService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("주문 생성 테스트")
    class createOrder {

        @Test
        @DisplayName("로그인하지 않은 유저가 주문시, 403 에러를 반환한다.")
        void test1() throws Exception {
            // Given
            OrderItemInfo orderItemInfo1 = OrderItemInfo.builder()
                    .itemId(1L)
                    .quantity(5)
                    .build();

            OrderItemInfo orderItemInfo2 = OrderItemInfo.builder()
                    .itemId(2L)
                    .quantity(3)
                    .build();

            OrderRequest request = OrderRequest.builder()
                    .addressId(1L)
                    .OrderItems(List.of(orderItemInfo1, orderItemInfo2))
                    .build();

            // expected
            mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }


        @Test
        @DisplayName("주문 성공시, 200 OK 를 반환한다.")
        @WithMockCustomUser(role = "BUYER")
        void test1000() throws Exception {
            // Given
            OrderItemInfo orderItemInfo1 = OrderItemInfo.builder()
                    .itemId(1L)
                    .quantity(5)
                    .build();

            OrderItemInfo orderItemInfo2 = OrderItemInfo.builder()
                    .itemId(2L)
                    .quantity(3)
                    .build();

            OrderRequest request = OrderRequest.builder()
                    .addressId(1L)
                    .OrderItems(List.of(orderItemInfo1, orderItemInfo2))
                    .build();

            // expected
            mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
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