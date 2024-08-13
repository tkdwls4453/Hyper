package com.dev.hyper.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomResponse <T>{
    private int returnCode;
    private String message;
    private T data;

    @Builder
    private CustomResponse(int returnCode, String message, T data) {
        this.returnCode = returnCode;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomResponse<T> OK(T data){
        return (CustomResponse<T>) CustomResponse.builder()
                .returnCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(data)
                .build();
    }
}
