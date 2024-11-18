package org.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequestDto {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String token;
}
