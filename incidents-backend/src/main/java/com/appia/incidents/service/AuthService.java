package com.appia.incidents.service;

import com.appia.incidents.dto.AuthResponse;
import com.appia.incidents.dto.LoginRequest;
import com.appia.incidents.entity.AppUser;
import com.appia.incidents.repository.AppUserRepository;
import com.appia.incidents.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.appia.incidents.exception.UnauthorizedException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {

        AppUser user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().name().toUpperCase()
        );

        return new AuthResponse(token);
    }
}
