package com.dev.hyper.category.repository;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CategorySelfJoinResult {
    private String name;
    private String parentName;
    private String childName;

    @QueryProjection
    public CategorySelfJoinResult(String name, String parentName, String childName) {
        this.name = name;
        this.parentName = parentName;
        this.childName = childName;
    }
}
