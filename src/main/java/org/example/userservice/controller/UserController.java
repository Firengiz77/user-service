package org.example.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("register")
    public String register(@ModelAttribute("user") User user) {
      return userService.createUser(user);
    }


}
