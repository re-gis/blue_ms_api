package com.merci.blue.controllers;

import com.merci.blue.dtos.CreateExamDto;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.ExamService;
import com.merci.blue.utils.Mapper;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createExam(@RequestParam("exam")MultipartFile exam, @RequestParam("answers") MultipartFile answers, @RequestParam("details") String details) throws IOException, ServiceException{
        try{
            // map the details
            CreateExamDto dto = Mapper.getDtoFromDetails(details);
            return ResponseEntity.ok(examService.createExam(dto.getLevel(), dto.getTerm(), dto.getCourse(), exam, answers));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
