package com.dev.hyper.store.controller;

import com.dev.hyper.auth.service.CustomUserDetailsService;
import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.common.config.SecurityConfig;
import com.dev.hyper.common.util.JwtUtil;
import com.dev.hyper.store.request.CreateStoreRequest;
import com.dev.hyper.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("[컨트롤러] 스토어 생성 테스트")
    class createStore {
        final String name = "test name";
        final String description = "test description";
        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("이름없이 스토어 생성을 요청하면, 400 에러를 반환한다.")
        void test1() throws Exception {
            // Given
            CreateStoreRequest request = CreateStoreRequest.builder()
//                    .name(name)
                    .description(description)
                    .build();

            // Expected
            mockMvc.perform(MockMvcRequestBuilders.post("/api/store")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("스토어 이름은 필수 입니다."))
                    .andDo(print())
            ;
        }

        @Test
        @WithMockCustomUser(username = "test@naver.com", role = "SELLER")
//        @WithMockUser(roles = "SELLER")
        @DisplayName("정상적인 스토어 생성을 요청하면, 200 OK를 반환한다.")
        void test1000() throws Exception {
            // Given
            CreateStoreRequest request = CreateStoreRequest.builder()
                    .name(name)
                    .description(description)
                    .build();

            // Expected
            mockMvc.perform(MockMvcRequestBuilders.post("/api/store")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }
}
