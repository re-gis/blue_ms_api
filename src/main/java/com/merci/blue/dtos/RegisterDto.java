package com.merci.blue.dtos;

import com.merci.blue.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String lastname;
    private String firstname;
    private String password;
    private String email;
    private String role;
}
