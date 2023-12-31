package com.merci.blue.controllers;

import com.merci.blue.dtos.CreateParentDto;
import com.merci.blue.dtos.CreateStudentDto;
import com.merci.blue.dtos.RegisterTeacher;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.UserService;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register/teacher")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> registerTeacher(@RequestBody RegisterTeacher dto) {
        try{
            return ResponseEntity.ok(userService.registerTeacher(dto));
        }catch(ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/register/parent")
    public ResponseEntity<ApiResponse<Object>> registerParent(@RequestBody CreateParentDto dto){
        try{
            return ResponseEntity.ok(userService.createParent(dto));
        }catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse<Object>> registerStudent(@RequestBody CreateStudentDto dto) {
        try{
            return ResponseEntity.ok(userService.createStudent(dto));
        }catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getMyProfile() {
        try {
            return ResponseEntity.ok(userService.getYourAccount());
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
