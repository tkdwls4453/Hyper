package com.dev.hyper.auth.dto.request;

import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SignUpRequest(

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 맞지 않습니다.")
        String email,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호 형식이 맞지 않습니다.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        String role
) {
        public User toEntity() {
                Role role = this.role == null ? Role.BUYER : Role.valueOf(this.role);

                return User.builder()
                        .email(this.email)
                        .password(this.password)
                        .name(this.name)
                        .role(role)
                        .build();
        }
}
