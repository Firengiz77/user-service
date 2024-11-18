package org.example.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String firstname;
    private String lastname;
    private String username;
    private float balance;
    private String email;
    private String token;
    private String role;

}
