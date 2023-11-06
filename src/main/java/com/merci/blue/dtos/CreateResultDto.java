package com.merci.blue.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateResultDto {
    private Long examId;
    private Long classId;
    private Long studentId;
}
