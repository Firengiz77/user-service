package org.example.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.userservice.model.Admin;
import org.example.userservice.repository.AdminRepository;
import org.example.userservice.service.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class AdminJwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final AdminRepository adminRepository;

    public AdminJwtRequestFilter(JwtTokenUtil jwtTokenUtil, AdminRepository adminRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtTokenUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                System.out.println("userdetails: " + userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);

        if (admin == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.emptyList()
        );
    }

}
