package com.merci.blue.services;

import com.merci.blue.entities.Attandance;
import com.merci.blue.entities.Class;
import com.merci.blue.entities.Student;
import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.AttandanceRepository;
import com.merci.blue.repositories.ClassRepository;
import com.merci.blue.repositories.StudentRepository;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttandanceService {
    private final AttandanceRepository attandanceRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final UserService userService;

    public ApiResponse createAttendance(Student student, LocalDate date, boolean present){

        if(student == null || date == null){
            throw new ServiceException("All attandance details are required!");
        }
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.TEACHER)){
            throw new ServiceException("You are not allowed to perform this action");
        }

        Teacher t = teacherRepository.findByFirstnameAndLastname(user.getFirstname(), user.getLastname()).orElseThrow(() -> new ServiceException("Teacher not found!"));

        // get class tutor
        Class cl = classRepository.findByTutor(t).orElseThrow(() -> new ServiceException("Class not found!"));

        boolean studentExistsInClass = cl.getStudents().stream()
                .anyMatch(st -> st.getId().equals(student.getId()));

        if(!studentExistsInClass){
            throw new ServiceException("Student not found in the class!");
        }

        // create the attendance for the student
        Attandance at = new Attandance();
        at.setDate(date);
        at.setStudent(student);
        at.setAClass(cl);
        at.setStudent(student);
        at.setPresent(present);

        attandanceRepository.save(at);
        return ApiResponse.builder()
                .success(true)
                .data(at)
                .build();
    }

}
