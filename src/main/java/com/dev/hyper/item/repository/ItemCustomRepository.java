package com.dev.hyper.item.repository;

import com.dev.hyper.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemCustomRepository {
    List<Item> findAllByProductId(Long productId);

    Boolean existsByIdAndUserEmail(Long itemId, String email);
}
