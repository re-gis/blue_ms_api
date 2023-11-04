package com.merci.blue.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateParentDto {
    private String firstname;
    private String lastname;
    private String contact;
    private Long student;
}
