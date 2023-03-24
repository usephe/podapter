package com.audiophileproject.usermanagement.controllers;


import com.audiophileproject.usermanagement.models.*;
import com.audiophileproject.usermanagement.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> signup(
            @RequestBody RegisterRequest request

    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/logout")
    public String logout() {
//        authService.logout();
        return "you are now logged out";
    }

    // TODO complete this endpoint
    @PostMapping("/refresh")
    public void authenticate(
            @RequestBody String refreshToken, HttpServletResponse response
    ) {

        try {
            new ObjectMapper().writeValue(response.getOutputStream(),authService.handleRefresh(refreshToken));
        } catch (Exception e) {
            response.setHeader("error", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", e.getMessage());
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            } catch (Exception ee) {
                ResponseEntity.internalServerError();
            }
        }
    }

}
