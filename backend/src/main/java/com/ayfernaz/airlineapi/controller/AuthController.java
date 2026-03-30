package com.ayfernaz.airlineapi.controller;

import com.ayfernaz.airlineapi.dto.AuthRequestDto;
import com.ayfernaz.airlineapi.dto.AuthResponseDto;
import com.ayfernaz.airlineapi.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            String token = jwtService.generateToken(request.getUsername());
            return new AuthResponseDto(token);
        }

        throw new RuntimeException("Invalid username or password");
    }
}