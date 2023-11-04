package com.merci.blue.dtos;

import com.merci.blue.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateClassDto {
    private String classname;
    private Long tutor;
}
