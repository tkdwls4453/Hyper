package com.dev.hyper.address.controller;

import com.dev.hyper.address.AddressService;
import com.dev.hyper.address.request.RegisterAddressRequest;
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
@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @MockBean
    private AddressService addressService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @WithMockCustomUser(role = "BUYER")
    @DisplayName("주소 등록 테스트")
    class registerAddress {

        @Test
        @DisplayName("주소의 제목없이 주소 등록시, 에러를 반환한다.")
        void test1() throws Exception {
            // Given
            RegisterAddressRequest request = RegisterAddressRequest.builder()
//                    .title("address")
                    .address("경기도 고양시")
                    .addressDetail("아파트 304호")
                    .code("13226")
                    .build();

            // Expected
            mockMvc.perform(post("/api/address")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("주소의 제목은 필수입니다."));
        }

        @Test
        @DisplayName("주소없이 주소 등록시, 에러를 반환한다.")
        void test2() throws Exception {
            // Given
            RegisterAddressRequest request = RegisterAddressRequest.builder()
                    .title("address")
//                    .address("경기도 고양시")
                    .addressDetail("아파트 304호")
                    .code("13226")
                    .build();

            // Expected
            mockMvc.perform(post("/api/address")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("주소는 필수입니다."));
        }

        @Test
        @DisplayName("주소의 코드없이 주소 등록시, 에러를 반환한다.")
        void test3() throws Exception {
            // Given
            RegisterAddressRequest request = RegisterAddressRequest.builder()
                    .title("address")
                    .address("경기도 고양시")
                    .addressDetail("아파트 304호")
//                    .code("13226")
                    .build();

            // Expected
            mockMvc.perform(post("/api/address")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("주소 코드는 필수입니다."));
        }

        @Test
        @DisplayName("주소를 정상적으로 등록시, 200 OK 를 반환한다.")
        void test1000() throws Exception {
            // Given
            RegisterAddressRequest request = RegisterAddressRequest.builder()
                    .title("address")
                    .address("경기도 고양시")
                    .addressDetail("아파트 304호")
                    .code("13226")
                    .build();

            // Expected
            mockMvc.perform(post("/api/address")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"));
        }
    }
}