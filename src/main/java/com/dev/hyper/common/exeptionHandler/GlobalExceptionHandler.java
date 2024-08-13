package com.dev.hyper.common.exeptionHandler;

import com.dev.hyper.common.response.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> bindingException(MethodArgumentNotValidException e){
        log.error("[error] message: {}", e.getAllErrors());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.ERROR(400, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CustomResponse> globalException(Exception e){
        log.error("[error] message: {}", e.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomResponse.ERROR(e));
    }
}
