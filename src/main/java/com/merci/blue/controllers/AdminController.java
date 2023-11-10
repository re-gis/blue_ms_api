package com.merci.blue.controllers;

import com.merci.blue.dtos.AssignCourseDto;
import com.merci.blue.dtos.DeleteStDto;
import com.merci.blue.dtos.DeleteTrDto;
import com.merci.blue.dtos.RegisterAdminDto;
import com.merci.blue.exceptions.ServiceException;
import com.merci.blue.response.ApiResponse;
import com.merci.blue.services.AdminService;
import com.merci.blue.services.TeacherService;
import com.merci.blue.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createAdmin(@RequestBody RegisterAdminDto dto) {
        try {
            return ResponseEntity.ok(adminService.registerAdmin(dto));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/teacher/all")
    public ResponseEntity<ApiResponse<Object>> getALlTeachers() {
        try {
            return ResponseEntity.ok(adminService.getAllTeachers());
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/teacher/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteTeacher(@PathVariable("id") Long id,
            @RequestBody DeleteTrDto dto) {
        try {
            return ResponseEntity.ok(adminService.deleteTeacher(id, dto.getPassword()));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/all")
    public ResponseEntity<ApiResponse<Object>> getAllStudents() {
        try {
            return ResponseEntity.ok(adminService.getAllStudents());
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/student/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteStudent(@PathVariable("id") Long id,
            @RequestBody DeleteStDto dto) {
        try {
            return ResponseEntity.ok(adminService.deleteStudent(id, dto.getPassword()));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/assign/{courseId}")
    public ResponseEntity<ApiResponse<Object>> assignCourseToTr(@PathVariable("courseId") Long courseId,
            @RequestBody AssignCourseDto dto) {
        try {
            return ResponseEntity.ok(adminService.assignCourseToTr(dto.getTeacherId(), courseId));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachers/leaves")
    public ResponseEntity<ApiResponse<Object>> getALlTrLeaves() {
        try {
            return ResponseEntity.ok(adminService.getAllTrLeaves());
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teachers/leaves/search")
    public ResponseEntity<ApiResponse<Object>> getLeavesByStatus(@RequestParam(name = "status") String status) {
        try {
            // get query
            if (status != null || !status.isEmpty()) {
                return ResponseEntity.ok(adminService.getLeavesByStatus(status));
            } else {
                return ResponseEntity.ok(adminService.getAllTrLeaves());
            }
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/teachers/leaves/approve/{id}")
    public ResponseEntity<ApiResponse<Object>> approveLeave(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(adminService.approveLeave(id));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/teachers/leaves/reject/{id}")
    public ResponseEntity<ApiResponse<Object>> rejectLeave(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(adminService.rejectLeave(id));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/teachers/assign/class/{id}")
    public ResponseEntity<ApiResponse<Object>> assignClassTutor(@PathVariable("id") Long classId, @RequestBody AssignCourseDto dto) {
        try {
            return ResponseEntity.ok(adminService.assignClass(classId, dto.getTeacherId()));
        } catch (ServiceException e) {
            return ResponseHandler.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
