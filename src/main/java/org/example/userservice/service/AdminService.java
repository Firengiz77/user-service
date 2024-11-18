package org.example.userservice.service;

import org.example.userservice.dto.request.AdminRequestDto;
import org.example.userservice.dto.response.AdminDto;
import org.example.userservice.dto.response.UserDto;
import org.example.userservice.exception.EmailAlreadyException;
import org.example.userservice.exception.IncorrectPassword;
import org.example.userservice.exception.NotFoundUserException;
import org.example.userservice.map.AdminMap;
import org.example.userservice.map.UserMap;
import org.example.userservice.model.Admin;
import org.example.userservice.model.User;
import org.example.userservice.repository.AdminRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AdminMap adminMap;
    private final UserMap userMap;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public AdminService(PasswordEncoder passwordEncoder,
                        AdminRepository adminRepository,
                        AdminMap adminMap,
                        JwtTokenUtil jwtTokenUtil,
                        UserRepository userRepository,
                        UserMap userMap) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.adminMap = adminMap;
        this.userMap = userMap;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    public AdminDto createUser(AdminRequestDto request) {

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyException(request.getEmail());
        }
        Admin admin = adminMap.fromRequest(request);
        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setUsername(generateRandomUsername());
        adminRepository.save(admin);

        return  generateTokenAdminDto(admin.getUsername(),admin);
    }

    public AdminDto updateUser(Admin request, String token) {

        String username = getUsernameToken(token);
        Admin admin = adminRepository.findByUsername(username);
        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        if (request.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        AdminDto adminDto = adminMap.toAdminDto(adminRepository.save(admin));
        adminDto.setToken(token);

        return adminDto;
    }

    public AdminDto updatePassword(String password, String token) {

        String username = getUsernameToken(token);
        Admin admin = adminRepository.findByUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        AdminDto adminDto = adminMap.toAdminDto(adminRepository.save(admin));
        adminDto.setToken(token);

        return adminDto;
    }

    public AdminDto login(String email, String password) {

        if (!adminRepository.existsByEmail(email)) {
            throw new NotFoundUserException();
        }

        Admin admin = adminRepository.findByEmail(email);

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IncorrectPassword(email);
        }

        return  generateTokenAdminDto(admin.getUsername(),admin);
    }

    private String getUsernameToken(String token){
        return jwtTokenUtil.extractUsername(token);
    }

    private AdminDto generateTokenAdminDto(String username,Admin admin) {
        String token = jwtTokenUtil.generateAdminToken(username);
        AdminDto adminDto = adminMap.toAdminDto(admin);
        adminDto.setToken(token);
        return adminDto;
    }

    private String generateRandomUsername() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);

        return String.valueOf(randomNumber);
    }

    public List<UserDto> getUsers(String token) {
        String username = getUsernameToken(token);
        Admin admin = adminRepository.findByUsername(username);
        List<User> users = userRepository.findAll();
        return userMap.toDto(users);
    }
}
