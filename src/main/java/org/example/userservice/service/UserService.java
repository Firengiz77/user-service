package org.example.userservice.service;

import org.example.userservice.Role;
import org.example.userservice.dto.UserDto;
import org.example.userservice.exception.NotFoundUserException;
import org.example.userservice.exception.EmailAlreadyException;
import org.example.userservice.map.UserMap;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMap userMap;
    private final JwtTokenUtil jwtTokenUtil; // JWT token utility


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMap userMap, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMap = userMap;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserDto createUser(User request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyException(request.getEmail());
        }


        User user = new User();
        user.setFirstname(request.getFirstname());
//        user.setUsername(request.getFirstname().toLowerCase(Locale.ROOT)+request.getLastname().toLowerCase());
        user.setUsername(generateRandomUsername());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String token = jwtTokenUtil.generateToken(user.getUsername(),user.getRole());
        UserDto userDto = userMap.toUserDto(userRepository.save(user));
        userDto.setToken(token);

        return userDto;
    }


    public UserDto updateUser(User request, String token) {
        String username = jwtTokenUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userMap.toUserDto(userRepository.save(user));
    }

    private String generateRandomUsername() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Generate a random number between 100000 and 999999
        return String.valueOf(randomNumber);
    }


//    public String login(String username, String password) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//
//        final UserDetails userDetails = loadUserByUsername(username);
//        return jwtTokenUtil.generateToken(userDetails);
//    }



}
