package com.merci.blue.controllers;

import com.merci.blue.dtos.CreateLeaveDto;
import com.merci.blue.dtos.DeleteStDto;
import com.merci.blue.dtos.DeleteTrDto;
import com.merci.blue.dtos.RegisterTeacher;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.TeacherService;
import com.merci.blue.utils.Mapper;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneTeacher(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(teacherService.getOneTeacher(id));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> updateTeacher(@RequestBody RegisterTeacher dto, @PathVariable("id") Long id) {
        try{
            return ResponseEntity.ok(teacherService.updateTrProfile(dto, id));
        }catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/leave/create")
    public ResponseEntity<ApiResponse<Object>> createALeave(@RequestParam("letter") MultipartFile letter,@RequestParam("details") String string) throws IOException, ServiceException {
        try{
            // map the req body
            CreateLeaveDto dto = Mapper.getDtoFromDetailsForLeave(string);
            return ResponseEntity.ok(teacherService.makeALeave(letter, dto.getReason()));
        }catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/leave/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteLeave(@PathVariable("id") Long id,@RequestBody DeleteStDto dto){
        try{
            return ResponseEntity.ok(teacherService.deleteLeave(id, dto.getPassword()));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
