package org.example.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.userservice.dto.request.AdminRequestDto;
import org.example.userservice.dto.response.AdminDto;
import org.example.userservice.dto.response.UserDto;
import org.example.userservice.model.Admin;
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
    public AdminDto register(@RequestBody AdminRequestDto admin) {
        return adminService.createUser(admin);
    }

    @PostMapping("/admin/login")
    public AdminDto login(@RequestParam String email, @RequestParam String password) {
        return adminService.login(email,password);
    }

    @PutMapping("/admin/update")
    public AdminDto update(@RequestBody Admin admin, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        System.out.println("token"+token);
        return adminService.updateUser(admin,token);
    }

    @PutMapping("/admin/update/password")
    public AdminDto updatePassword(@RequestParam String password, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return adminService.updatePassword(password,token);
    }


    @GetMapping("/admin/users")
    public List<UserDto>  getUsers(HttpServletRequest httpServletRequest){
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return adminService.getUsers(token);
    }
}
