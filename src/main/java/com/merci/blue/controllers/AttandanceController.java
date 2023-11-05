package com.merci.blue.controllers;

import com.merci.blue.dtos.martAttDto;
import com.merci.blue.entities.Student;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.repositories.StudentRepository;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.AttandanceService;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attandance")
@RequiredArgsConstructor
public class AttandanceController {
    private final AttandanceService attandanceService;
    private final StudentRepository studentRepository;

    @PostMapping("/mark")
    public ResponseEntity<ApiResponse<Object>> markAttandance(@RequestBody martAttDto dto){
        try{
            if(dto.getStId() == null){
                throw new ServiceException("Student id required!");
            }
            // get student
            Student st = studentRepository.findById(dto.getStId()).orElseThrow(() -> new ServiceException("Student not found!"));
            return ResponseEntity.ok(attandanceService.createAttendance(st, dto.getDate(), dto.isPresent()));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
