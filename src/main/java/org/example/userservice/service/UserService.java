package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.request.UserRequestDto;
import org.example.userservice.enums.Permission;
import org.example.userservice.enums.Role;
import org.example.userservice.dto.response.UserDto;
import org.example.userservice.exception.EmailAlreadyException;
import org.example.userservice.exception.IncorrectPassword;
import org.example.userservice.exception.NotFoundUserException;
import org.example.userservice.exception.PermissionDeniedException;
import org.example.userservice.map.UserMap;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMap userMap;
    private final JwtTokenUtil jwtTokenUtil;

    public UserDto createUser(UserRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyException(request.getEmail());
        }
        User user = userMap.fromRequest(request);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(generateRandomUsername());
        userRepository.save(user);

        return  generateTokenUserDto(user.getUsername(),user.getRole(),user);
    }

    public UserDto updateUser(User request, String token) {
        String username = getUsernameToken(token);
        User user = userRepository.findByUsername(username);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        UserDto userDto = userMap.toUserDto(userRepository.save(user));
        userDto.setToken(token);
        return userDto;
    }

    public UserDto updatePassword(String password, String token) {
        String username = getUsernameToken(token);
        User user = userRepository.findByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        UserDto userDto = userMap.toUserDto(userRepository.save(user));
        userDto.setToken(token);
        return userDto;
    }

    public UserDto login(String email, String password) {

        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundUserException();
        }

        User user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPassword(email);
        }

        if (!hasPermission(user.getRole(), Permission.LOGIN_AS_USER)) {
            throw new PermissionDeniedException();
        }

        return  generateTokenUserDto(user.getUsername(),user.getRole(),user);
    }

    private String getUsernameToken(String token){
        return jwtTokenUtil.extractUsername(token);
    }

    private UserDto generateTokenUserDto(String username,Role role,User user) {
        String token = jwtTokenUtil.generateToken(username,role);
        UserDto userDto = userMap.toUserDto(user);
        userDto.setToken(token);
        return userDto;
    }

    private String generateRandomUsername() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }

    public boolean hasPermission(Role role, Permission permission) {
        return role.getPermissions().contains(permission);
    }

    public UserDto updateBalance(float balance, String token) {
        String username = getUsernameToken(token);
        User user = userRepository.findByUsername(username);
        float newBalance = user.getBalance() + balance;
        user.setBalance(newBalance);
        return userMap.toUserDto(userRepository.save(user));
    }
}