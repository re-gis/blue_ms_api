package com.merci.blue.dtos;

import com.merci.blue.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterAdminDto {
    private String lastname;
    private String firstname;
    private ERole role;
    private String password;
    private  String code;
}
