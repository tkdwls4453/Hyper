package com.dev.hyper.common.filter;


import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.auth.dto.request.SignInRequest;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.common.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.setFilterProcessesUrl("/open-api/auth/login");
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    public LoginFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager1;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    private final Long EXPIRED_MS = 24 * 60 * 60 * 1000L;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SignInRequest signInRequest = null;

        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            signInRequest = objectMapper.readValue(messageBody, SignInRequest.class);
        } catch (IOException e) {
            throw new CustomErrorException(ErrorCode.INVALID_SIGN_IN_REQUEST_ERROR);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password());

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        Iterator<? extends GrantedAuthority> iterator = principal.getAuthorities().iterator();
        GrantedAuthority role = iterator.next();

        String jwt = jwtUtil.createJwt(principal.getUsername(), role.getAuthority().split("_")[1], EXPIRED_MS);

        CustomResponse<String> body = CustomResponse.OK(jwt);

        response.addHeader("Authorization", "Bearer " + jwt);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        CustomResponse errorResponse = CustomResponse.builder()
                .returnCode(400)
                .message("로그인 실패")
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
