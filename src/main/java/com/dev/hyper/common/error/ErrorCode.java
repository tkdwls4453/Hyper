package com.dev.hyper.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TEST_ERROR(400, 1500, "테스트용 에러입니다."),
    SERVER_ERROR(500, 500, "서버 내부 에러입니다."),

    // 유저 관련 에러는 1000 번대 사용
    ALREADY_EXISTS_EMAIL_ERROR(400, 1400, "이미 가입된 이메일입니다."),
    INVALID_SIGN_IN_REQUEST_ERROR(400,1401 , "잘못된 로그인 요청입니다."),
    NOT_FOUND_USER_ERROR(400, 1402, "존재하지 않는 유저입니다."),

    // 제품 관련 에러는 2000 번대 사용
    PRODUCT_CREATION_PERMISSION_ERROR(403, 2403, "제품 생성 권한이 없습니다."),
    PRODUCT_UPDATE_PERMISSION_ERROR(403, 2404, "제품 수정 권한이 없습니다."),
    PRODUCT_NOT_FOUND_ERROR(404, 2405, "존재하지 않는 제품입니다."),

    // 아이템 관련 에러는 3000 번대 사용
    ITEM_PERMISSION_ERROR(403, 3403, "아이템 접근 권한이 없습니다."),
    ITEM_NOT_FOUND_ERROR(404, 3404, "존재하지 않는 아이템입니다."),

    INVALID_STOCK_ERROR(400, 3405, "유효하지 않는 재고량입니다."),

    CATEGORY_NOT_FOUND_ERROR(400, 4400, "존재하지 않는 카테고리 입니다."),
    ;

    private final int httpCode;
    private final int returnCode;
    private final String message;
}
