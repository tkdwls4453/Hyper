package com.dev.hyper.store.service;

import com.dev.hyper.store.domain.Store;
import com.dev.hyper.store.repository.StoreRepository;
import com.dev.hyper.store.request.CreateStoreRequest;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.groups.Tuple.*;

// TODO: properties 를 좀 더 깔끔하게 처리하는 법 찾아보자
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class StoreServiceTest {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        storeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @DisplayName("승인되지 않은 상점을 생성하고 데이터베이스에 저장한다.")
    void createStore(){
        // Given
        User user = User.builder()
                .email("test@naver.com")
                .name("test")
                .password("tsettest!@")
                .role(Role.SELLER)
                .build();

        User savedUser = userRepository.save(user);

        CreateStoreRequest request = CreateStoreRequest.builder()
                .name("test name")
                .description("test description")
                .build();

        // When
        storeService.createStore(request, "test@naver.com");

        // Then
        List<Store> result = storeRepository.findAll();
        assertThat(result).hasSize(1)
                .extracting(
                        "name", "description", "isAccepted"
                )
                .containsExactlyInAnyOrder(
                        Tuple.tuple("test name", "test description", false)
                );

        Store store = result.get(0);
        assertThat(store.getUser()).isEqualTo(savedUser);
    }
}