package org.example.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.service.AdminService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("/admin/register")
    public UserDto register(@RequestBody User user) {
        return adminService.createUser(user);
    }

    @PostMapping("/admin/login")
    public UserDto login(@RequestParam String email, @RequestParam String password) {
        return adminService.login(email,password);
    }

    @PutMapping("/admin/update")
    public UserDto update(@RequestBody User user, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return adminService.updateUser(user,token);
    }

    @PutMapping("/admin/update/password")
    public UserDto updatePassword(@RequestParam String password, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return adminService.updatePassword(password,token);
    }

    @GetMapping("/admin/users")
    public List<UserDto> getUsers() {
        return adminService.getUsers();
    }

}
