package com.appia.incidents.controller;

import org.springframework.web.bind.annotation.*;
import com.appia.incidents.dto.AuthResponse;
import com.appia.incidents.dto.LoginRequest;
import com.appia.incidents.service.AuthService;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}