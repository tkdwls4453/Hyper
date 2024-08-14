package com.dev.hyper.common;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .email(customUser.username())
                .role(Role.valueOf(customUser.role()))
                .build();

        UserPrincipal userPrincipal = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken auth = UsernamePasswordAuthenticationToken.authenticated(userPrincipal, "password", userPrincipal.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }
}
