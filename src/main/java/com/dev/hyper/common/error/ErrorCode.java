package com.dev.hyper.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    TEST_ERROR(400, 1500, "테스트용 에러입니다."),
    SERVER_ERROR(500, 500, "서버 내부 에러입니다.");

    private final int httpCode;
    private final int returnCode;
    private final String message;
}
