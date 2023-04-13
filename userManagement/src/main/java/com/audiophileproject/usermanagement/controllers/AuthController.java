package com.audiophileproject.usermanagement.controllers;


import com.audiophileproject.usermanagement.models.*;
import com.audiophileproject.usermanagement.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> signup(
            @RequestBody RegisterRequest request,
            HttpServletResponse response

    ) {
        var authenticationResponse = authService.register(request);
        setHttpOnlyRefreshTokenCookie(response, authenticationResponse.getRefreshToken());
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        var authenticationResponse = authService.authenticate(request);
        setHttpOnlyRefreshTokenCookie(response, authenticationResponse.getRefreshToken());
        return ResponseEntity.ok(authenticationResponse);
    }

    private void setHttpOnlyRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        // Set the refresh token in an HttpOnly cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(-1);
        response.addCookie(refreshTokenCookie);
    }

    @PostMapping("/logout")
    public String logout() {
//        authService.logout();
        return "you are now logged out";
    }

    // TODO complete this endpoint
    @GetMapping(value = "/refresh")
    public void authenticate(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {

        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
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
