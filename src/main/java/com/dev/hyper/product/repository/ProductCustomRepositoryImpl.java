package com.dev.hyper.product.repository;

import com.dev.hyper.product.repository.dto.ProductQueryResult;
import com.dev.hyper.product.repository.dto.QProductQueryResult;
import com.dev.hyper.product.request.FilterDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                .orderBy(sortCondition(sortingCondition), product.id.asc())
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

    @Override
    public Page<ProductQueryResult> search(String search, String sortingCondition, FilterDto filterDto, Pageable pageable) {
        List<ProductQueryResult> content = queryFactory
                .select(
                        new QProductQueryResult(
                                product.id,
                                product.name,
                                product.description,
                                category.name,
                                product.price,
                                product.createdAt,
                                product.updatedAt
                        )
                )
                .from(product)
                .where(
                        searchContains(search),
                        categoryEq(filterDto),
                        priceGoe(filterDto),
                        priceLoe(filterDto)
                )
                .orderBy(sortCondition(sortingCondition), product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(product)
                .from(product)
                .where(
                        searchContains(search),
                        categoryEq(filterDto),
                        priceGoe(filterDto),
                        priceLoe(filterDto)
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchContains(String search) {
        return search != null ? product.name.contains(search) : null;
    }

    private BooleanExpression categoryEq(FilterDto filterDto) {
        if(filterDto.category() == null) return null;

        return filterDto.category() != null ? product.category.name.eq(filterDto.category()) : null;
    }

    private BooleanExpression priceGoe(FilterDto filterDto) {
        if(filterDto.priceRange() == null) return null;

        return filterDto.priceRange().get(0) != null ? product.price.goe(filterDto.priceRange().get(0)) : null;
    }

    private BooleanExpression priceLoe(FilterDto filterDto) {
        if(filterDto.priceRange() == null) return null;

        return filterDto.priceRange().get(1) != null ? product.price.loe((filterDto.priceRange().get(1))) : null;
    }

    private OrderSpecifier<?> sortCondition(String sortingCondition) {
        if (sortingCondition == null) {
            return product.id.asc();
        }
        return switch (sortingCondition){
            case "latest" -> product.updatedAt.asc();
            case "oldest" -> product.updatedAt.desc();
            case "cheap" -> product.price.asc();
            case "expensive" -> product.price.desc();
            default -> product.id.asc();
        };
    }
}
