package com.dev.hyper.user.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {

    BUYER("구매자"),
    SELLER("판매자"),
    ADMIN("관리자");

    private final String message;
}
