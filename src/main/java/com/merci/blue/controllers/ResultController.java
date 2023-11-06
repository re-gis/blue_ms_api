package com.merci.blue.controllers;

import com.merci.blue.dtos.CreateResultDto;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.ResultService;
import com.merci.blue.utils.Mapper;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/result")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createResult(@RequestParam("results") MultipartFile file, @RequestParam("details") String data)throws IOException, ServiceException {
        try{
            CreateResultDto dto = Mapper.getDtoFromDetailsForResult(data);
            return ResponseEntity.ok(resultService.createResultDoc(file, dto.getClassId(), dto.getExamId(), dto.getStudentId()));
        }catch (ServiceException e){
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
