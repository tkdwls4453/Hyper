package com.dev.hyper.item;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.item.repository.ItemRepository;
import com.dev.hyper.item.request.CreateItemRequest;
import com.dev.hyper.product.ProductRepository;
import com.dev.hyper.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;
    public void createItem(CreateItemRequest request, Long productId, String email){

        Product product = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.PRODUCT_NOT_FOUND_ERROR);
                }
        );

        if (!product.getUser().getEmail().equals(email)) {
            throw new CustomErrorException(ErrorCode.ITEM_CREATION_PERMISSION_ERROR);
        }

        Item item = request.toEntity();
        item.updateProduct(product);

        itemRepository.save(item);
    }
}
