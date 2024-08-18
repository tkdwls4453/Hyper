package com.dev.hyper.item.repository;

import com.dev.hyper.item.Item;
import com.dev.hyper.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import static com.dev.hyper.item.QItem.*;
import static com.dev.hyper.product.domain.QProduct.*;
import static com.dev.hyper.user.domain.QUser.*;

public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    @PersistenceContext
    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public ItemCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Item> findAllByProductId(Long productId) {
        return queryFactory
                .select(item)
                .from(item)
                .join(item.product, product)
                .where(product.id.eq(productId))
                .fetch();
    }

    @Override
    public Boolean existsByIdAndUserEmail(Long itemId, String email) {
        return queryFactory
                .select(item)
                .from(item)
                .join(item.product, product)
                .join(product.user, user)
                .where(
                        item.id.eq(itemId),
                        user.email.eq(email)
                )
                .fetchCount() > 0;
    }

}
