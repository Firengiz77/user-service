package org.example.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public String login() {
        return "Successfully Login";
    }

    @PostMapping("/user/register")
    public UserDto register(@RequestBody User user) {
      return userService.createUser(user);
    }

   @PostMapping("/user/update")
    public UserDto update(@RequestBody User user, HttpServletRequest httpServletRequest) {
       String authorizationHeader = httpServletRequest.getHeader("Authorization");
       String token = null;
       if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
           token = authorizationHeader.substring(7);
       }

       return userService.updateUser(user,token);
   }

}
