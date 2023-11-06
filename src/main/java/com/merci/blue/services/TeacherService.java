package com.merci.blue.services;

import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserService userService;


    public ApiResponse getAllTeachers(){
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action!");
        }

        // return all teachers
        List<Teacher> teachers = teacherRepository.findAll();
        if(teachers.isEmpty()) {
            throw new ServiceException("No teachers found!");
        }
        return ApiResponse.builder()
                .success(true)
                .data(teachers)
                .build();
    }

    public ApiResponse getOneTeacher(Long id){
        return ApiResponse.builder()
                .success(true)
                .data(teacherRepository.findById(id).orElseThrow(() -> new ServiceException("Teacher not found!")))
                .build();
    }
}
