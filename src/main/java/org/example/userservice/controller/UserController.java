package org.example.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.userservice.dto.request.UserRequestDto;
import org.example.userservice.dto.response.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public UserDto register(@RequestBody UserRequestDto user) {
        return userService.createUser(user);
    }

    @PostMapping("/user/login")
    public UserDto login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email,password);
    }

    @PutMapping("/user/update")
    public UserDto update(@RequestBody User user, HttpServletRequest httpServletRequest) {
       String authorizationHeader = httpServletRequest.getHeader("Authorization");
       String token = null;
       if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
           token = authorizationHeader.substring(7);
       }
       return userService.updateUser(user,token);
   }

    @PutMapping("/user/update/password")
    public UserDto updatePassword(@RequestParam String password, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return userService.updatePassword(password,token);
    }

    @PutMapping("/user/update/balance")
    public UserDto updateBalance(@RequestParam float balance, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return userService.updateBalance(balance,token);
    }

}
