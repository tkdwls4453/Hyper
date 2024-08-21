package com.dev.hyper.category.repository;

import com.dev.hyper.category.QCategory;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.dev.hyper.category.QCategory.*;

public class CategoryCustomRepositoryImpl implements CategoryCustomRepository{

    private final JPAQueryFactory queryFactory;

    public CategoryCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CategorySelfJoinResult> findAllSelfJoin() {
        QCategory child = new QCategory("child");
        QCategory parent = new QCategory("parent");

        return queryFactory
                .select(
                        new QCategorySelfJoinResult(category.name, parent.name, child.name)
                )
                .from(category)
                .leftJoin(category.parent, parent)
                .leftJoin(category.children, child)
                .fetch();
    }
}
