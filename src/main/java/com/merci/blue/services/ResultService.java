package com.merci.blue.services;

import com.merci.blue.entities.*;
import com.merci.blue.entities.Class;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.*;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.utils.UploadDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final ResultRepository resultRepository;
    private final ExamRepository examRepository;
    private final UploadDoc uploadDoc;
    private final UserService userService;
    private final StudentRepository studentRepository;

    public ApiResponse<Object> createResultDoc(MultipartFile result, Long classId, Long examId, Long stId)
            throws IOException, ServiceException {
        // user logged in
        User user = userService.getLoggedUser();

        // get teacher
        Teacher t = teacherRepository.findByFirstnameAndLastname(user.getFirstname(), user.getLastname())
                .orElseThrow(() -> new ServiceException("Teacher not found!"));
        // get class
        Class c = classRepository.findByTutor(t).orElseThrow(() -> new ServiceException("Class not found!"));
        if (!c.getTutor().equals(t)) {
            throw new ServiceException("You are not authorised to perform this action!");
        }

        // get student
        Student st = studentRepository.findById(stId).orElseThrow(() -> new ServiceException("Student not found!"));

        // check if student exists in class
        if (!c.getStudents().contains(st)) {
            throw new ServiceException("Student not found in this class");
        }

        // get exam
        Exam e = examRepository.findById(examId).orElseThrow(() -> new ServiceException("Exam not found!"));

        // check if the student result isn't created
        Optional<Result> rs = resultRepository.findByStudentAndExam(st, e);
        if (rs.isPresent()) {
            if (rs.get().getExam().getTerm().equals(e.getTerm())) {
                throw new ServiceException("Student already has results!");
            }
        }

        // upload the doc
        String resultDoc = uploadDoc.uploadDoc(result);
        // create result in database
        var r = Result.builder()
                .exam(e)
                .classname(c)
                .resultdoc(resultDoc)
                .student(st)
                .build();
        resultRepository.save(r);

        return ApiResponse.builder()
                .success(true)
                .data(r)
                .build();
    }
}
