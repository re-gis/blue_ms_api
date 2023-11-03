package com.merci.blue.services;

import com.merci.blue.dtos.RegisterTeacher;
import com.merci.blue.entities.Course;
import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.EGender;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.CourseRepository;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.repositories.UserRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse registerTeacher(RegisterTeacher dto){
        if(dto.getAddress() == null || dto.getDob() == null || dto.getContact() == null || dto.getRole() == null || dto.getFirstname() == null || dto.getLastname() == null || dto.getCourseId() == null){
            throw new ServiceException("ALl teacher details are required!");
        }

        // create the code for the teacher
        Random random = new Random();

        // Generate a random six-digit integer
        int min = 1000;
        int max = 9999;
        int randomNumber = random.nextInt(max - min + 1) + min;

        Optional<Teacher> t = teacherRepository.findOneByContact(dto.getContact());
        if(t.isPresent()) {
            throw  new ServiceException("Teacher already exists");
        }

        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ServiceException("Course not found!"));
        // create the teacher
        var teacher = Teacher.builder()
                .dob(dto.getDob())
                .address(dto.getAddress())
                .contact(dto.getContact())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .gender(EGender.MALE)
                .code(randomNumber)
                .build();

        teacher.addCourse(course);

        // create user
        var user = User.builder()
                .lastname(dto.getLastname())
                .firstname(dto.getFirstname())
                .password(passwordEncoder.encode(String.valueOf(randomNumber)))
                .role(ERole.TEACHER)
                .build();

        // save both user && teacher
        userRepository.save(user);
        teacherRepository.save(teacher);

        return ApiResponse.builder()
                .success(true)
                .data("Teacher registered successfully...")
                .build();
    }
}
