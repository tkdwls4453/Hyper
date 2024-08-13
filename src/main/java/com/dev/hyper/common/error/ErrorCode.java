package com.dev.hyper.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TEST_ERROR(400, 1500, "테스트용 에러입니다."),
    SERVER_ERROR(500, 500, "서버 내부 에러입니다."),

    // 유저 관련 에러는 1000 번대 사용
    ALREADY_EXISTS_EMAIL(400, 1400, "이미 가입된 이메일입니다."),
    INVALID_SIGN_IN_REQUEST(400,1401 , "잘못된 로그인 요청입니다.");

    private final int httpCode;
    private final int returnCode;
    private final String message;
}
