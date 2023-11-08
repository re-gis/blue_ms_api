package com.merci.blue.services;

import com.merci.blue.dtos.RegisterTeacher;
import com.merci.blue.entities.Class;
import com.merci.blue.entities.Course;
import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.EGender;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.ClassRepository;
import com.merci.blue.repositories.CourseRepository;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.repositories.UserRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;



    public ApiResponse getOneTeacher(Long id){
        return ApiResponse.builder()
                .success(true)
                .data(teacherRepository.findById(id).orElseThrow(() -> new ServiceException("Teacher not found!")))
                .build();
    }

    // update teacher profile
    public ApiResponse updateTrProfile(RegisterTeacher dto, Long teacherId){
        // get teacher
        Teacher t = teacherRepository.findOneById(teacherId).orElseThrow(() -> new ServiceException("Teacher not found!"));
        User u = userRepository.findByLastnameAndFirstnameAndRoleNot(t.getLastname(), t.getFirstname(), ERole.STUDENT).orElseThrow(() -> new ServiceException("User not found!"));

        if(dto.getGender() != null && dto.getGender() != ""){
            String g = dto.getGender().toLowerCase();
            switch (g){
                case "male":
                    t.setGender(EGender.MALE);
                    break;

                case "female":
                    t.setGender(EGender.FEMALE);
                    break;

                case "other":
                    t.setGender(EGender.OTHER);
                    break;

                default:
                    throw new ServiceException("Gender not allowed!");
            }
        }

        if(dto.getRole() !=null && dto.getRole() !=""){
            String r = dto.getRole().toLowerCase();

            switch (r){
                case "admin":
                    u.setRole(ERole.ADMIN);
                    break;
                case "teacher":
                    u.setRole(ERole.TEACHER);
                    break;
                default:
                    throw new ServiceException("Role not allowed for the teacher");
            }
        }

        if(dto.getLastname() != null && dto.getLastname() != ""){
            t.setLastname(dto.getLastname());
            u.setLastname(dto.getLastname());
        }

        if(dto.getFirstname() !=null && dto.getFirstname()!=""){
            t.setFirstname(dto.getFirstname());
            u.setFirstname(dto.getFirstname());
        }

        if(dto.getContact() != null && dto.getContact() != ""){
            t.setContact(dto.getContact());
        }

        if(dto.getAddress() != null && dto.getAddress() != ""){
            t.setAddress(dto.getAddress());
        }

        if(dto.getCourseId() !=null && dto.getCourseId() != 0){
            // get course
            Course c = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ServiceException("Course not found!"));
            t.addCourse(c);
            c.setTeacher(t);
            courseRepository.save(c);
        }

        if(dto.getDob() != null){
            t.setDob(dto.getDob());
        }

        // save the all repos
        userRepository.save(u);
        teacherRepository.save(t);

        return ApiResponse.builder()
                .success(true)
                .data("Teacher profile updated successfully!")
                .build();
    }
}
