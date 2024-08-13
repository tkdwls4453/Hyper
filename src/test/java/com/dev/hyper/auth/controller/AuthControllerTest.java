package com.dev.hyper.auth.controller;

import com.dev.hyper.auth.dto.request.SignUpRequest;
import com.dev.hyper.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원가입 테스트")
    class signUp{
        final String email = "test@naver.com";
        final String password = "fkjbkafb!12";
        final String name = "test";
        final String role = "BUYER";

        @Test
        @DisplayName("이메일 없이 회원가입을 요청하면, 400 에러를 반환한다.")
        void test1() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
//                    .email(email)
                    .password(password)
                    .name(name)
                    .role(role)
                    .build();

            // Expcted
            String body = objectMapper.writeValueAsString(request);
            mockMvc.perform(MockMvcRequestBuilders.post("/open-api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이메일은 필수입니다."))
                    .andDo(print());

        }

        @Test
        @DisplayName("형식이 맞지 않는 이메일로 회원가입을 요청하면, 400 에러를 반환한다.")
        void test2() throws Exception {
            // Given
            final String invalidEmail = "thisistestemail";

            SignUpRequest request = SignUpRequest.builder()
                    .email(invalidEmail)
                    .password(password)
                    .name(name)
                    .role(role)
                    .build();

            // Expected
            String body = objectMapper.writeValueAsString(request);
            mockMvc.perform(MockMvcRequestBuilders.post("/open-api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이메일 형식이 맞지 않습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("비밀번호 없이 회원가입을 요청하면, 400 에러를 반환한다.")
        void test3() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
//                    .password(password)
                    .name(name)
                    .role(role)
                    .build();

            // Expected
            String body = objectMapper.writeValueAsString(request);
            mockMvc.perform(MockMvcRequestBuilders.post("/open-api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("형식이 맞지 않는 비밀번호로 회원가입을 요청하면, 400 에러를 반환한다.")
        void test4() throws Exception {
            final String invalidPassword = "thisisTestpassword";
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(invalidPassword)
                    .name(name)
                    .role(role)
                    .build();

            // Expected
            String body = objectMapper.writeValueAsString(request);
            mockMvc.perform(MockMvcRequestBuilders.post("/open-api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("비밀번호 형식이 맞지 않습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("이름 없이 회원가입을 요청하면, 400 에러를 반환한다.")
        void test5() throws Exception {
            final String invalidPassword = "thisisTestpassword";
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
//                    .name(name)
                    .role(role)
                    .build();

            // Expected
            String body = objectMapper.writeValueAsString(request);
            mockMvc.perform(MockMvcRequestBuilders.post("/open-api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이름은 필수입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("회원가입을 요청하면, 200 OK를 반환한다.")
        void test1000() throws Exception {
            final String invalidPassword = "thisisTestpassword";
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .role(role)
                    .build();

            // Expected
            String body = objectMapper.writeValueAsString(request);
            mockMvc.perform(MockMvcRequestBuilders.post("/open-api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andDo(print());
        }
    }
}