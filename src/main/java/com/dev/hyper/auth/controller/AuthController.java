package com.dev.hyper.auth.controller;

import com.dev.hyper.auth.dto.request.SignUpRequest;
import com.dev.hyper.auth.service.AuthService;
import com.dev.hyper.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 인증 관련 API", description = "유저 회원가입 및 인증 관련 기능을 제공합니다.")
@AllArgsConstructor
@RestController
@RequestMapping("/open-api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "새로운 유저를 회원으로 등록합니다."
    )
    @PostMapping("/signup")
    public CustomResponse<String> signUp(
            @RequestBody @Valid SignUpRequest request
    ){
        authService.signUp(request);
        return CustomResponse.OK();
    }
}