package com.merci.blue.controllers;

import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.TeacherService;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getALlTeachers() {
        try {
            return ResponseEntity.ok(teacherService.getAllTeachers());
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneTeacher(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(teacherService.getOneTeacher(id));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
