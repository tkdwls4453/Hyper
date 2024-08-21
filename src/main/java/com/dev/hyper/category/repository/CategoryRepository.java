package com.dev.hyper.category.repository;

import com.dev.hyper.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {
    Optional<Category> findByName(String name);
}
