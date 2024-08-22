package com.dev.hyper.product.repository;

import com.dev.hyper.category.QCategory;
import com.dev.hyper.product.domain.QProduct;
import com.dev.hyper.product.repository.dto.ProductQueryResult;
import com.dev.hyper.product.repository.dto.QProductQueryResult;
import com.dev.hyper.product.response.ProductResponse;
import com.dev.hyper.user.domain.QUser;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.dev.hyper.category.QCategory.*;
import static com.dev.hyper.product.domain.QProduct.*;
import static com.dev.hyper.user.domain.QUser.*;

public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory queryFactory;

    public ProductCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProductQueryResult> findAllByUserEmailWithSorting(Pageable pageable, String sortingCondition, String email) {
        List<ProductQueryResult> content = queryFactory
                .select(new QProductQueryResult(
                        product.id,
                        product.name,
                        product.description,
                        category.name,
                        product.price,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .join(product.user, user)
                .join(product.category, category)
                .where(user.email.eq(email))
                .orderBy(createCondition(sortingCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(product)
                .from(product)
                .join(product.user, user)
                .where(user.email.eq(email))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> createCondition(String sortingCondition) {
        return switch (sortingCondition){
            case "latest" -> product.updatedAt.asc();
            case "oldest" -> product.updatedAt.desc();
            case "cheap" -> product.price.asc();
            case "expensive" -> product.price.desc();
            default -> product.id.asc();
        };
    }
}
