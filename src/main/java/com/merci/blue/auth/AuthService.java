package com.merci.blue.auth;

import com.merci.blue.dtos.LoginDto;
import com.merci.blue.entities.User;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.UserRepository;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse loginUser(LoginDto dto) {
        if (dto.getCode() == null || dto.getPassword() == null) {
            throw new ServiceException("All credentials are required!");
        }

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getCode(), dto.getPassword())
        );

        if(!auth.isAuthenticated()) {
            throw  new ServiceException("Authentication failed");
        }

        User user = userRepository.findByCode(dto.getCode()).orElseThrow(() -> new ServiceException("User not found!"));

        System.out.println(user);
        var token = jwtService.generateToken(user);
        return ApiResponse.builder()
                .success(true)
                .data(token)
                .build();
    }
}
