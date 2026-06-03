package com.catchprice.apiservice.service;

import com.catchprice.apiservice.dto.AuthRequest;
import com.catchprice.apiservice.dto.AuthResponse;
import com.catchprice.apiservice.dto.RegisterRequest;
import com.catchprice.apiservice.model.User;
import com.catchprice.apiservice.repositories.UserRepository;
import com.catchprice.apiservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .cep(request.cep())
                .build();
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getName(), user.getEmail());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(user), user.getName(), user.getEmail());
    }
}
