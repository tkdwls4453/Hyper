package com.dev.hyper.exeptionHandler;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.common.response.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MIN_VALUE)
@RestControllerAdvice
public class CustomErrorExceptionHandler {

    @ExceptionHandler(value = CustomErrorException.class)
    public ResponseEntity<CustomResponse> customException(CustomErrorException e) {
        log.error("[error] error: {}, message: {}", e.getErrorCode() , e.getMessage());
        ErrorCode code = e.getErrorCode();

        return ResponseEntity
                .status(code.getHttpCode())
                .body(CustomResponse.ERROR(code));
    }
}
