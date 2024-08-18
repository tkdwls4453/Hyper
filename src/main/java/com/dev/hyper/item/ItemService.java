package com.dev.hyper.item;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.item.repository.ItemRepository;
import com.dev.hyper.item.request.CreateItemRequest;
import com.dev.hyper.item.request.UpdateItemRequest;
import com.dev.hyper.item.response.ItemResponse;
import com.dev.hyper.product.ProductRepository;
import com.dev.hyper.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new CustomErrorException(ErrorCode.ITEM_PERMISSION_ERROR);
        }

        Item item = request.toEntity();
        item.updateProduct(product);

        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getItems(Long productId, String email) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.PRODUCT_NOT_FOUND_ERROR);
                }
        );

        if (!product.getUser().getEmail().equals(email)) {
            throw new CustomErrorException(ErrorCode.ITEM_PERMISSION_ERROR);
        }

        List<Item> items = itemRepository.findAllByProductId(productId);

        return items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());

    }

    public void updateInfo(Long itemId, UpdateItemRequest request, String email) {

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.ITEM_NOT_FOUND_ERROR);
                }
        );

        if(!itemRepository.existsByIdAndUserEmail(itemId, email)){
            throw new CustomErrorException(ErrorCode.ITEM_PERMISSION_ERROR);
        }



        item.updateInfo(request);
    }

    public void deleteItem(Long itemId, String email) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.ITEM_NOT_FOUND_ERROR);
                }
        );

        if(!itemRepository.existsByIdAndUserEmail(itemId, email)){
            throw new CustomErrorException(ErrorCode.ITEM_PERMISSION_ERROR);
        }

        itemRepository.deleteById(itemId);
    }
}
