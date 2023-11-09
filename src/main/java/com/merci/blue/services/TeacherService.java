package com.merci.blue.services;

import com.merci.blue.dtos.RegisterTeacher;
import com.merci.blue.entities.*;
import com.merci.blue.entities.Class;
import com.merci.blue.enums.EGender;
import com.merci.blue.enums.ERole;
import com.merci.blue.enums.EStatus;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.*;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.utils.UploadDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LeaveRepository leaveRepository;
    private final UploadDoc uploadDoc;
    private final AuthenticationManager authenticationManager;



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

    // make a leave
    public ApiResponse makeALeave(MultipartFile letter, String reason) throws IOException, ServiceException {
        User u = userService.getLoggedUser();
        if(!u.getRole().equals(ERole.TEACHER)){
            throw new ServiceException("You are not allowed to perform this action!");
        }

        if(reason == null || reason.isEmpty() || letter == null) {
            throw new ServiceException("All leave details are required!");
        }

        // get teacher
        Teacher t = teacherRepository.findByFirstnameAndLastname(u.getFirstname(), u.getLastname()).orElseThrow(() -> new ServiceException("Teacher not found!"));

        Optional<Leave> l = leaveRepository.findByTeacher(t);
        if(l.isPresent()){
            throw new ServiceException("Leave already made...");
        }

        // upload a letter
        String doc = uploadDoc.uploadDoc(letter);
        if(doc == null){
            throw new ServiceException("Error while uploading the letter...");
        }

        // create leave
        var leave = Leave.builder()
                .reason(reason)
                .resignationLetter(doc)
                .status(EStatus.PENDING)
                .teacher(t)
                .build();

        leaveRepository.save(leave);

        return ApiResponse
                .builder()
                .success(true)
                .data("Leave is made successfully...")
                .build();
    }

    public ApiResponse deleteLeave(Long leave, String password) {
        User u = userService.getLoggedUser();
        if(!u.getRole().equals(ERole.TEACHER)){
            throw new ServiceException("You are not authorised to perform this action!");
        }

        if(password == null || password.isEmpty()){
            throw new ServiceException("Password is required!");
        }

        // get teacher
        Teacher t = teacherRepository.findByFirstnameAndLastname(u.getFirstname(), u.getLastname()).orElseThrow(()-> new ServiceException("Teacher not found!"));

        // get the leave
        Leave l = leaveRepository.findById(leave).orElseThrow(() -> new ServiceException("No leave found!"));

        if(!l.getTeacher().getId().equals(t.getId())){
            throw new ServiceException("You are not allowed to delete this leave");
        }


        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(u.getCode(), password)
        );
        if(!auth.isAuthenticated()) {
            throw  new ServiceException("Authentication failed");
        }

        leaveRepository.delete(l);
        return ApiResponse.builder()
                .success(true)
                .data("Leave deleted successfully...")
                .build();
    }
}
