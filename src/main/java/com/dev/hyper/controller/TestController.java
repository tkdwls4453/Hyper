package com.dev.hyper.controller;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.common.response.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public CustomResponse<String> test(){
        return CustomResponse.OK("테스트 성공");
    }

    @GetMapping("/error2")
    public CustomResponse<String> error(){
        System.out.println("test");
        throw new CustomErrorException(ErrorCode.TEST_ERROR);
    }

    @GetMapping("/server-error")
    public CustomResponse<String> serverError(){
        int a = 10 / 0;

        return CustomResponse.OK("테스트 성공");
    }
}
