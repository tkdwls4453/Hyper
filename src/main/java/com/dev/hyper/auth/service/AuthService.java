package com.dev.hyper.auth.service;

import com.dev.hyper.auth.dto.request.SignUpRequest;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUp(SignUpRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new CustomErrorException(ErrorCode.ALREADY_EXISTS_EMAIL);
        }

        String encodePassword = bCryptPasswordEncoder.encode(request.password());
        User user = request.toEntity(encodePassword);

        userRepository.save(user);
    }
}
