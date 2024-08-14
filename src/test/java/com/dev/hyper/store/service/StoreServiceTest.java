package com.dev.hyper.store.service;

import com.dev.hyper.store.domain.Store;
import com.dev.hyper.store.repository.StoreRepository;
import com.dev.hyper.store.request.CreateStoreRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.groups.Tuple.*;

// TODO: properties 를 좀 더 깔끔하게 처리하는 법 찾아보자
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("승인되지 않은 상점을 생성하고 데이터베이스에 저장한다.")
    void createStore(){
        // Given
        CreateStoreRequest request = CreateStoreRequest.builder()
                .name("test name")
                .description("test description")
                .build();

        // When
        storeService.createStore(request);

        // Then
        List<Store> result = storeRepository.findAll();
        Assertions.assertThat(result).hasSize(1)
                .extracting(
                        "name", "description", "isAccepted"
                )
                .containsExactlyInAnyOrder(
                        tuple("test name", "test description", false)
                );
    }
}