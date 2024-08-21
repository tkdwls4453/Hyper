package com.dev.hyper.category.repository;

import com.dev.hyper.category.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryCustomRepositoryImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("카테고리를 셀프 조인해서 조회한다.")
    void findAllSelfJoin(){

        // Given
        Category parent = Category.builder()
                .name("parent")
                .build();

        Category category = Category.builder()
                .name("category")
                .parent(parent)
                .build();

        Category child1 = Category.builder()
                .name("child1")
                .parent(category)
                .build();

        Category child2 = Category.builder()
                .name("child2")
                .parent(category)
                .build();

        categoryRepository.saveAll(List.of(parent, category, child1, child2));

        // When
        List<CategorySelfJoinResult> result = categoryRepository.findAllSelfJoin();

        // Then
        assertThat(result).hasSize(5)
                .extracting("name", "parentName", "childName")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("parent", null, "category"),
                        Tuple.tuple("category", "parent", "child1"),
                        Tuple.tuple("category", "parent", "child2"),
                        Tuple.tuple("child1", "category", null),
                        Tuple.tuple("child2", "category", null)
                );
    }

}