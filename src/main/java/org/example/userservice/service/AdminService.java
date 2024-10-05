package org.example.userservice.service;

import org.example.userservice.dto.UserDto;
import org.example.userservice.enums.Role;
import org.example.userservice.exception.EmailAlreadyException;
import org.example.userservice.exception.IncorrectPassword;
import org.example.userservice.exception.NotFoundUserException;
import org.example.userservice.map.UserMap;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMap userMap;
    private final JwtTokenUtil jwtTokenUtil;

    public AdminService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMap userMap, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMap = userMap;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserDto createUser(User request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyException(request.getEmail());
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .username(generateRandomUsername())
                .build();

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

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findByRole(Role.USER);
        List<UserDto> userDto = userMap.toDto(users);

        return userDto;
    }
}
