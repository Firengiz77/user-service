package org.example.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String token;
}
