package com.merci.blue.controllers;

import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.StudentService;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getAllStudents(){
        try{
            return ResponseEntity.ok(studentService.getAllStudents());
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneStudent(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(studentService.getOneStudent(id));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
