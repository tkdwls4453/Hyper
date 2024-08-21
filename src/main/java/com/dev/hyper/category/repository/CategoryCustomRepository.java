package com.dev.hyper.category.repository;

import java.util.List;

public interface CategoryCustomRepository {

    List<CategorySelfJoinResult> findAllSelfJoin();
}
