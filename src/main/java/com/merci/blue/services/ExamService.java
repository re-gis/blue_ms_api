package com.merci.blue.services;

import com.merci.blue.entities.Course;
import com.merci.blue.entities.Exam;
import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.CourseRepository;
import com.merci.blue.repositories.ExamRepository;
import com.merci.blue.repositories.TeacherRepository;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.utils.UploadDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final UserService userService;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final UploadDoc uploadDoc;

    public ApiResponse createExam(String level, String term, Long course, MultipartFile exam, MultipartFile answer) throws IOException, ServiceException {
        if(term == null || level == null || exam == null || answer == null || course == null){
            throw new ServiceException("All exam details are required!");
        }
        User user = userService.getLoggedUser();
        if(!user.getRole().equals(ERole.TEACHER)){
            throw new ServiceException("You are not allowed to perform this action!");
        }

        Teacher t = teacherRepository.findByFirstnameAndLastname(user.getFirstname(), user.getLastname()).orElseThrow(() -> new ServiceException("Teacher not found!"));
        // create exam
        // get course
        Course c = courseRepository.findById(course).orElseThrow(() -> new ServiceException("Course not found!"));
        String name = c.getCoursename() + "-" + level + "-" + term;
        String examUrl = uploadDoc.uploadDoc(exam);
        String sheetUrl = uploadDoc.uploadDoc(answer);
        var e = Exam.builder()
                .course(c)
                .level(level)
                .answersheet(sheetUrl)
                .term(term)
                .examdoc(examUrl)
                .examname(name)
                .build();

        examRepository.save(e);
        return ApiResponse.builder()
                .success(true)
                .data(e)
                .build();
    }
}
