package com.merci.blue.services;

import com.merci.blue.dtos.CreateParentDto;
import com.merci.blue.dtos.CreateStudentDto;
import com.merci.blue.dtos.RegisterTeacher;
import com.merci.blue.entities.*;
import com.merci.blue.enums.EGender;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.*;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    public ApiResponse registerTeacher(RegisterTeacher dto){

        //check if he is an admin
        User user = getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action");
        }


        if(dto.getAddress() == null || dto.getDob() == null || dto.getContact() == null || dto.getRole() == null || dto.getFirstname() == null || dto.getLastname() == null || dto.getCourseId() == null || dto.getGender() == null){
            throw new ServiceException("ALl teacher details are required!");
        }

        if(!dto.getRole().equalsIgnoreCase("teacher")) {
            throw  new ServiceException("User role not allowed!, add Teacher as role...");
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
                .code(randomNumber)
                .build();

        if(dto.getGender().equalsIgnoreCase("male")) {
            teacher.setGender(EGender.MALE);
        } else if (dto.getGender().equalsIgnoreCase("female")) {
            teacher.setGender(EGender.FEMALE);
        }else {
            teacher.setGender(EGender.OTHER);
        }

        teacher.addCourse(course);


        // create user
        var eUser = User.builder()
                .lastname(dto.getLastname())
                .firstname(dto.getFirstname())
                .password(passwordEncoder.encode(String.valueOf(randomNumber)))
                .code(String.valueOf(randomNumber))
                .role(ERole.TEACHER)
                .build();

        // save both user && teacher
        userRepository.save(eUser);
        teacherRepository.save(teacher);
        course.setTeacher(teacher);
        courseRepository.save(course);
        return ApiResponse.builder()
                .success(true)
                .data("Teacher registered successfully...")
                .build();
    }

    public User getLoggedUser() throws ServiceException {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()== "anonymousUser") {
            throw  new ServiceException(("You are not logged in"));
        }

        String code;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails) {
            code = ((UserDetails) principal).getUsername();
        } else {
            code = principal.toString();
        }
        return userRepository.findByCode(code).orElseThrow(() -> new ServiceException("Could not find user!"));
    }

    public ApiResponse createParent(CreateParentDto dto) {
        User user = getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)) {
            throw new ServiceException("You are not authorised to perform this action!");
        }

        if(dto.getContact() == null || dto.getFirstname() == null || dto.getStudent() == null || dto.getLastname() == null){
            throw new ServiceException("All parent details are required!");
        }

        // check if parent already exists
        Optional<Parent> pr = parentRepository.findOneByContact(dto.getContact());
        if(pr.isPresent()){
            throw new ServiceException("Parent already exists...");
        }

        Student st = studentRepository.findById(dto.getStudent()).orElseThrow(() -> new ServiceException("Student not found!"));

        // create the parent and update the student
        var parent = Parent.builder()
                .student(st)
                .lastname(dto.getLastname())
                .firstname(dto.getFirstname())
                .contact(dto.getContact())
                .build();

        st.setParent(parent);
        parentRepository.save(parent);
        studentRepository.save(st);
        return ApiResponse.builder()
                .success(true)
                .data(parent)
                .build();
    }

    public ApiResponse createStudent(CreateStudentDto dto) {
        User user = getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)) {
            throw new ServiceException("You are not authorised to perform this action!");
        }

        if(dto.getContact() == null || dto.getLastname() == null || dto.getFirstname() == null || dto.getDob() == null || dto.getAddress() == null || dto.getGender() == null){
            throw new ServiceException("All student details are required!");
        }

        Optional<Student> st = studentRepository.findOneByContact(dto.getContact());
        if(st.isPresent()){
            throw new ServiceException("Student already exists!");
        }

        // create student && user
        var student = Student.builder()
                .address(dto.getAddress())
                .contact(dto.getContact())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .dob(dto.getDob())
                .build();

        if(dto.getGender().equalsIgnoreCase("male")){
            student.setGender(EGender.MALE);
        } else if (dto.getGender().equalsIgnoreCase("female")) {
            student.setGender(EGender.FEMALE);
        }else {
            student.setGender(EGender.OTHER);
        }

        Random random = new Random();

        int min = 1000;
        int max = 9999;
        int randomNumber = random.nextInt(max - min + 1) + min;

        // create user
        var u = User.builder()
                .role(ERole.STUDENT)
                .lastname(dto.getLastname())
                .firstname(dto.getFirstname())
                .code(String.valueOf(randomNumber))
                .password(passwordEncoder.encode(String.valueOf(randomNumber)))
                .build();

        // save user and student
        studentRepository.save(student);
        userRepository.save(u);

        return ApiResponse.builder()
                .success(true)
                .data(student)
                .build();
    }
}
