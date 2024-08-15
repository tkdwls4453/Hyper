package com.dev.hyper.auth.service;

import com.dev.hyper.auth.dto.request.SignUpRequest;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Nested
    @DisplayName("signup 서비스 테스트")
    class signUp{
        final String email = "test@naver.com";
        final String password = "fkjbkafb!12";
        final String name = "test";

        @AfterEach
        void tearDown() {
            userRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("이미 가입한 이메일로 회원가입하면 예외를 반환한다.")
        void test1(){
            // Given
            userRepository.save(User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .role(Role.BUYER)
                    .build());

            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .build();

            // Expected
            assertThatThrownBy(() -> authService.signUp(request))
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage(ErrorCode.ALREADY_EXISTS_EMAIL_ERROR.getMessage());

        }

        @Test
        @DisplayName("처음 가입한 이메일로 회원가입하면 유저가 데이터베이스에 저장된다.")
        void test1000(){
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .build();

            // When
            authService.signUp(request);

            // Then
            List<User> result = userRepository.findAll();

            assertThat(result).hasSize(1)
                    .extracting("email", "name")
                    .containsExactlyInAnyOrder(
                            tuple(email, name)
                    );

            boolean isMatched = bCryptPasswordEncoder.matches(password, result.get(0).getPassword());
            assertThat(isMatched).isTrue();
        }
    }
}