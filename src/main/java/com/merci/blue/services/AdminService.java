package com.merci.blue.services;

import com.merci.blue.dtos.RegisterAdminDto;
import com.merci.blue.entities.*;
import com.merci.blue.entities.Class;
import com.merci.blue.enums.ERole;
import com.merci.blue.enums.EStatus;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.*;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClassRepository classRepository;
    private final UserService userService;

    private final TeacherRepository teacherRepository;

    private final CourseRepository courseRepository;

    private final StudentRepository studentRepository;

    private final AttandanceRepository attandanceRepository;
    private final ParentRepository parentRepository;
    private final ResultRepository resultRepository;
    private final LeaveRepository leaveRepository;

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


    /* TEACHER */

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



    // delete the teacher
    public ApiResponse deleteTeacher(Long teacherId, String password){
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not allowed to perform this action");
        }

        // authenticate the admin
        if(password == null || password ==""){
            throw new ServiceException("Enter the password to delete the user");
        }

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("admin", password)
        );
        if(!auth.isAuthenticated()) {
            throw  new ServiceException("Authentication failed");
        }

        // delete the teacher
        Teacher t = teacherRepository.findById(teacherId).orElseThrow(() -> new ServiceException("Teacher not found!"));
        for(Course course: t.getCourses()){
            course.setTeacher(null);
            courseRepository.save(course);
        }

        User u = userRepository.findByLastnameAndFirstnameAndRole(t.getLastname(), t.getFirstname(), ERole.TEACHER).orElseThrow(() -> new ServiceException("Teacher not found in users"));
        userRepository.delete(u);
        Optional<Class> c = classRepository.findByTutor(t);
        if(c.isPresent()){
            c.get().setTutor(null);
            classRepository.save(c.get());
        }
        teacherRepository.delete(t);
        return ApiResponse.builder()
                .success(true)
                .data("Teacher account deleted successfully...")
                .build();
    }



    /* STUDENT */

    public ApiResponse getAllStudents() {
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action");
        }

        // get all students
        List<Student> stds = studentRepository.findAll();
        if(stds.isEmpty()){
            throw new ServiceException("No students found!");
        }

        return ApiResponse.builder()
                .success(true)
                .data(stds)
                .build();
    }

    public ApiResponse deleteStudent(Long id, String password) {
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action!");
        }


        // authenticate the admin
        if(password == null || password ==""){
            throw new ServiceException("Enter the password to delete the student");
        }

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("admin", password)
        );
        if(!auth.isAuthenticated()) {
            throw  new ServiceException("Authentication failed");
        }

        // delete the teacher
        Student t = studentRepository.findById(id).orElseThrow(() -> new ServiceException("Student not found!"));

        // get his class
        Class c = classRepository.findById(t.getAClass().getId()).orElseThrow(() -> new ServiceException("Class not found!"));
        c.getStudents().remove(t);
        classRepository.save(c);

        // get attandance
        Optional<Attandance> at = attandanceRepository.findByStudent(t);
        if(at.isPresent()){
            // delete the attandance
            attandanceRepository.delete(at.get());
        }

        Optional<User> u = userRepository.findByLastnameAndFirstnameAndRole(t.getLastname(), t.getFirstname(), ERole.STUDENT);
        if(u.isPresent()) {
            userRepository.delete(u.get());
        }

        // delete parent
        Optional<Parent> p = parentRepository.findByStudent(t);
        if(p.isPresent()){
            parentRepository.delete(p.get());
        }

        // delete the results
        Optional<Result> r = resultRepository.findByStudent(t);
        if(r.isPresent()){
            resultRepository.delete(r.get());
        }

        studentRepository.delete(t);
        return ApiResponse.builder()
                .success(true)
                .data("Student account deleted successfully...")
                .build();
    }


    // assign teacher a course
    public ApiResponse assignCourseToTr(Long teacherId, Long courseId){
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action!");
        }

        // get teacher && course
        Teacher t = teacherRepository.findById(teacherId).orElseThrow(() -> new ServiceException("Teacher not found!"));
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("Course not found!"));

        // if found
        t.addCourse(c);
        c.setTeacher(t);
        teacherRepository.save(t);
        courseRepository.save(c);
        return ApiResponse.builder()
                .success(true)
                .data(String.format("Course assigned to teacher %s", t.getFirstname()))
                .build();
    }

    /* LEAVE MANAGEMENT */

    public ApiResponse getAllTrLeaves() {
        User u = userService.getLoggedUser();
        if(!u.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action!");
        }

        // get the leaves
        List<Leave> leaves = leaveRepository.findAll();
        if(leaves.isEmpty()){
            throw new ServiceException("No leaving letters found!");
        }

        return ApiResponse.builder()
                .success(true)
                .data(leaves)
                .build();
    }

    public ApiResponse getLeavesByStatus(String status){
        EStatus st;
        User u = userService.getLoggedUser();
        if(!u.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action!");
        }

        switch (status.toLowerCase()){
            case "approved":
                st = EStatus.APPROVED;
                break;

            case "pending":
                st = EStatus.PENDING;
                break;

            case "rejected":
                st = EStatus.REJECTED;
                break;

            default:
                throw new ServiceException("Status not allowed");
        }

        // get the leaves
        List<Leave> leaves = leaveRepository.findAllByStatus(st);
        if(leaves.isEmpty()){
            throw new ServiceException(String.format("No %s leaving letters found!", st));
        }

        return ApiResponse.builder()
                .success(true)
                .data(leaves)
                .build();
    }

    // approve
    public ApiResponse approveLeave(Long leave){
        User u = userService.getLoggedUser();
        if(!u.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action");
        }

        // approve
        Leave l = leaveRepository.findById(leave).orElseThrow(() -> new ServiceException("Leave not found!"));
        if(l.getStatus().equals(EStatus.APPROVED)){
            throw new ServiceException("Leave already approved...");
        }
        l.setStatus(EStatus.APPROVED);
        leaveRepository.save(l);
        return ApiResponse.builder()
                .success(true)
                .data("Leave approved successfully...")
                .build();
    }

    public ApiResponse rejectLeave(Long leave) {
        User u = userService.getLoggedUser();
        if(!u.getRole().equals(ERole.ADMIN)){
            throw new ServiceException("You are not authorised to perform this action");
        }

        Leave l = leaveRepository.findById(leave).orElseThrow(() -> new ServiceException("Leave not found!"));
        if(l.getStatus().equals(EStatus.REJECTED)){
            throw new ServiceException("Leave already rejected...");
        }

        l.setStatus(EStatus.REJECTED);
        leaveRepository.save(l);
        return ApiResponse.builder()
                .success(true)
                .data("Leave rejected successfully...")
                .build();
    }
}
