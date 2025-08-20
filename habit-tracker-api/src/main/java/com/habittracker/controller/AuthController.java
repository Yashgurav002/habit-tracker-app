package com.habittracker.controller;

import com.habittracker.dto.*;
import com.habittracker.entity.User;
import com.habittracker.security.JwtUtil;
import com.habittracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);
        
        String jwt = jwtUtil.generateTokenFromUserId(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken, user.getId(), user.getUsername(), 
                user.getEmail(), user.getFirstName(), user.getLastName());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", jwtResponse));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(
            userService.findByUsernameOrEmail(loginRequest.getUsernameOrEmail()).getId());
        
        User user = userService.findByUsernameOrEmail(loginRequest.getUsernameOrEmail());
        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken, user.getId(), user.getUsername(), 
                user.getEmail(), user.getFirstName(), user.getLastName());
        
        return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid refresh token", null));
        }
        
        try {
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            User user = userService.findById(userId);
            
            String newAccessToken = jwtUtil.generateTokenFromUserId(userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(userId);
            
            JwtResponse jwtResponse = new JwtResponse(newAccessToken, newRefreshToken, user.getId(), 
                    user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
            
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid refresh token", null));
        }
    }
}
