package com.yalchenko.task_manager.controller;

import com.yalchenko.task_manager.dto.auth.LoginRequest;
import com.yalchenko.task_manager.dto.auth.RegisterRequest;
import com.yalchenko.task_manager.dto.auth.JwtResponse;
import com.yalchenko.task_manager.entity.User;
import com.yalchenko.task_manager.entity.enums.Role;
import com.yalchenko.task_manager.exception.BadRequestException;
import com.yalchenko.task_manager.service.UserService;
import com.yalchenko.task_manager.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          JwtTokenProvider tokenProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.USER);

        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);
        User user = userService.findByEmail(request.getEmail());

        return ResponseEntity.ok(
                new JwtResponse(token, "Bearer", user.getUsername(), user.getRole().name())
        );
    }
}
