package com.merci.blue.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentDto {
    private String firstname;
    private String lastname;
    private Date dob;
    private String contact;
    private String address;
    private String classname;
    private String gender;
    private Long parent;
}
