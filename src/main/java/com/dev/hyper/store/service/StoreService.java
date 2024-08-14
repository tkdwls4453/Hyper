package com.dev.hyper.store.service;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.store.domain.Store;
import com.dev.hyper.store.repository.StoreRepository;
import com.dev.hyper.store.request.CreateStoreRequest;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    public void createStore(CreateStoreRequest request, String userEmail) {

        // ??? 이미 컨트롤러에 들어올 때 유저 권한을 찾아서 들어오는데 여기서 다시 예외처리를 해야하나?
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.NOT_FOUND_USER);
                }
        );

        Store store = request.toEntity();
        store.updateUser(user);

        storeRepository.save(store);
    }
}
