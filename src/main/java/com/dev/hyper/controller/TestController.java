package com.dev.hyper.controller;

import com.dev.hyper.common.response.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public CustomResponse<String> test(){
        return CustomResponse.OK("테스트 성공");
    }
}
