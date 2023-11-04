package com.merci.blue.services;

import com.merci.blue.dtos.CreateCourseDto;
import com.merci.blue.entities.Course;
import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.CourseRepository;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.repositories.UserRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApiResponse createACourse(CreateCourseDto dto) {
        if(dto.getDescription() == null || dto.getCoursename() == null){
            throw new ServiceException("All course details are required!");
        }

        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException(("You are not authorised to perform this action!"));
        }

        // create the code for the teacher
        Random random = new Random();

        // Generate a random six-digit integer
        int min = 100;
        int max = 999;
        int randomNumber = random.nextInt(max - min + 1) + min;

        // create the course
        Optional<Course> eC = courseRepository.findByCoursename(dto.getCoursename());

        if(eC.isPresent()){
            throw new ServiceException("Course already exists...");
        }

        var course = Course.builder()
                .code(String.valueOf(randomNumber))
                .description(dto.getDescription())
                .coursename(dto.getCoursename())
                .build();

        if(dto.getTeacher() != null) {
            // get the teacher
            Teacher t = teacherRepository.findById(dto.getTeacher()).orElseThrow(() -> new ServiceException("Teacher not found!"));
            course.setTeacher(t);
            t.addCourse(course);
            courseRepository.save(course);
        }

        courseRepository.save(course);
        return ApiResponse.builder()
                .success(true)
                .data(course)
                .build();
    }
}
