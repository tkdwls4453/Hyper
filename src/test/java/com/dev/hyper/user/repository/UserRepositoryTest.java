package com.dev.hyper.user.repository;

import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
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

    @Test
    @DisplayName("findByEmail 테스트")
    void findByEmail(){
        // Given
        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.BUYER)
                .build());

        // When
        User result = userRepository.findByEmail(email).orElse(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }
}