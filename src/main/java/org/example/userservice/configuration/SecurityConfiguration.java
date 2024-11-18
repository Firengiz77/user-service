package org.example.userservice.configuration;

import org.example.userservice.filter.AdminJwtRequestFilter;
import org.example.userservice.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;
    private final AdminJwtRequestFilter adminJwtRequestFilter;

    public SecurityConfiguration(JwtRequestFilter jwtRequestFilter,AdminJwtRequestFilter adminJwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.adminJwtRequestFilter = adminJwtRequestFilter;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/user/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/user/register", "/user/login").permitAll()
                        .requestMatchers("/user/update","/user/update/password").hasAuthority("USER")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/login","/admin/register","/admin/users","/admin/update","/admin/update/password","admin/users").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(adminJwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
}
