package com.dev.hyper.product.repository;

import com.dev.hyper.product.repository.dto.ProductQueryResult;
import com.dev.hyper.product.request.FilterDto;
import com.dev.hyper.product.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {
    Page<ProductQueryResult> findAllByUserEmailWithSorting(Pageable pageable, String sortingCondition, String email);
    Page<ProductQueryResult> search(String search, String sortingCondition, FilterDto filterDto, Pageable pageable);
}
