package com.merci.blue.dtos;

import com.merci.blue.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTeacher {
    private String lastname;
    private String firstname;
    private Date dob;
    private String role;
    private String contact;
    private String address;
    private Long courseId;
}
