package com.dev.hyper.common.response;

import com.dev.hyper.common.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T> {
    private int returnCode;
    private String message;
    private T data;

    @Builder
    private CustomResponse(int returnCode, String message, T data) {
        this.returnCode = returnCode;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomResponse<T> OK(T data) {
        return (CustomResponse<T>) CustomResponse.builder()
                .returnCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(data)
                .build();
    }

    public static <T> CustomResponse<T> OK() {
        return (CustomResponse<T>) CustomResponse.builder()
                .returnCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();
    }

    public static CustomResponse ERROR(ErrorCode code) {
        return CustomResponse.builder()
                .returnCode(code.getReturnCode())
                .message(code.getMessage())
                .build();
    }

    public static CustomResponse ERROR(Exception e) {
        return CustomResponse.builder()
                .returnCode(ErrorCode.SERVER_ERROR.getReturnCode())
                .message(e.getLocalizedMessage())
                .build();
    }

    public static CustomResponse ERROR(int code, String message) {
        return CustomResponse.builder()
                .returnCode(code)
                .message(message)
                .build();
    }

}
