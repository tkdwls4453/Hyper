package com.dev.hyper.category;

import com.dev.hyper.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    String name;

    @OneToMany(mappedBy = "parent")
    List<Category> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Category parent;

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void updateParent(Category parentCategory) {
        this.parent = parentCategory;
        parentCategory.getChildren().add(this);
    }
}
