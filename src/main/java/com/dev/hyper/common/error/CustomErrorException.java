package com.dev.hyper.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomErrorException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
