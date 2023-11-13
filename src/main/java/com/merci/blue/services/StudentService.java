package com.merci.blue.services;

import com.merci.blue.entities.Student;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.StudentRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;

    public ApiResponse<Object> getOneStudent(Long id) {
        return ApiResponse.builder()
                .success(true)
                .data(studentRepository.findById(id).orElseThrow(() -> new ServiceException("Student not found!")))
                .build();
    }
}
