package com.dev.hyper.product;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.product.request.UpdateProductRequest;
import com.dev.hyper.store.domain.Store;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public void createProduct(CreateProductRequest request, String email) {
        Product product = request.toEntity();

        // 컨트롤러단에서 이미 유저를 검증하여 따로 예외처리가 필요없음
        User user = userRepository.findByEmail(email).orElse(null);

        if (!user.getRole().equals(Role.SELLER)) {
            throw new CustomErrorException(ErrorCode.PRODUCT_CREATION_PERMISSION_ERROR);
        }

        Store store = user.getStore();

        product.updateUser(user);
        product.updateStore(store);

        productRepository.save(product);
    }

    public void updateProduct(UpdateProductRequest request, Long productId, String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (!user.getRole().equals(Role.SELLER)) {
            throw new CustomErrorException(ErrorCode.PRODUCT_UPDATE_PERMISSION_ERROR);
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.PRODUCT_NOT_FOUND_ERROR);
                }
        );

        product.updateInfo(request);

    }
}
