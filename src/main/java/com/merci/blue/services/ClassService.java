package com.merci.blue.services;

import com.merci.blue.dtos.CreateClassDto;
import com.merci.blue.entities.Class;
import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.ClassRepository;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final UserService userService;

    public ApiResponse<Object> createClass(CreateClassDto dto) {
        User u = userService.getLoggedUser();
        if (!u.getRole().equals(ERole.ADMIN)) {
            throw new ServiceException("You are not allowed to perform this action!");
        }

        if (dto.getTutor() == null | dto.getClassname() == null) {
            throw new ServiceException("All class details are required!");
        }

        // check if class exists
        Optional<Class> cl = classRepository.findByClassname(dto.getClassname());

        if (cl.isPresent()) {
            throw new ServiceException("Class already exists");
        }

        Teacher tutor = teacherRepository.findById(dto.getTutor())
                .orElseThrow(() -> new ServiceException("Tutor not found!"));

        // create class
        var c = Class.builder()
                .classname(dto.getClassname())
                .tutor(tutor)
                .build();

        classRepository.save(c);

        return ApiResponse.builder()
                .success(true)
                .data(c)
                .build();
    }
}
