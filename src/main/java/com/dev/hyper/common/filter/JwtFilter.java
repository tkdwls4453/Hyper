package com.dev.hyper.common.filter;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.util.JwtUtil;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            log.info("token 이 만료됐습니다.");
            filterChain.doFilter(request, response);
            return;
        }


        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        log.info("[jwt 정보] email: {}, role: {}", email, role);

        UserPrincipal userPrincipal = new UserPrincipal(User.builder()
                .email(email)
                .role(Role.valueOf(role))
                .build());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
