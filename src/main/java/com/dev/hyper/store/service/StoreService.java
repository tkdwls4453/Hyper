package com.dev.hyper.store.service;

import com.dev.hyper.store.domain.Store;
import com.dev.hyper.store.repository.StoreRepository;
import com.dev.hyper.store.request.CreateStoreRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public void createStore(CreateStoreRequest request) {
        Store store = request.toEntity();
        storeRepository.save(store);
    }
}
