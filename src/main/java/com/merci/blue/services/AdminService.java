package com.merci.blue.services;

import com.merci.blue.dtos.RegisterAdminDto;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.UserRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse registerAdmin(RegisterAdminDto dto) {
        if(dto.getPassword() == null || dto.getCode() == null || dto.getFirstname() == null || dto.getLastname() == null){
            throw  new ServiceException("All admin details are required");
        }

        if(!dto.getPassword().equals("admin@password") || !dto.getCode().equals("admin")) {
            throw  new ServiceException("Invalid credentials");
        }

        var admin = userRepository.findByLastnameAndFirstname(dto.getLastname(), dto.getFirstname());
        if(admin.isPresent()){
            throw new ServiceException("Admin already exists...");
        }

        // create the user
        var user = User.builder()
                .role(ERole.ADMIN)
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .code(dto.getCode())
                .build();

        // save the user
        userRepository.save(user);
        return ApiResponse.builder()
                .success(true)
                .data(user)
                .build();
    }
}
