package com.dev.hyper.user.repository;

import com.dev.hyper.auth.dto.request.SignUpRequest;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    final String email = "test@naver.com";
    final String password = "fkjbkafb!12";
    final String name = "test";

    @Test
    @DisplayName("existsByEmail 테스트")
    void existsByEmail(){
        // Given
        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.BUYER)
                .build());

        // When
        boolean result = userRepository.existsByEmail(email);

        // Then
        assertThat(result).isTrue();
    }
}