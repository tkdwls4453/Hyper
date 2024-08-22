package com.dev.hyper.product.repository;


import com.dev.hyper.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
}
