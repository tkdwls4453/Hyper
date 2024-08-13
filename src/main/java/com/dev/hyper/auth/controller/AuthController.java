package com.dev.hyper.auth.controller;

import com.dev.hyper.auth.dto.request.SignUpRequest;
import com.dev.hyper.auth.service.AuthService;
import com.dev.hyper.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/open-api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public CustomResponse<String> signUp(
            @RequestBody @Valid SignUpRequest request
    ){
        authService.signUp(request);
        return CustomResponse.OK();
    }
}
