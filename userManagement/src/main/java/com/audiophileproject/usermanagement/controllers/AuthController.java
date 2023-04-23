package com.audiophileproject.usermanagement.controllers;


import com.audiophileproject.usermanagement.models.*;
import com.audiophileproject.usermanagement.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request

    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/logout")
    public String logout() {
        // let's do it on the client side only
//        authService.logout();
        return "you are now logged out";
    }

    @PostMapping(value = "/refresh")
    public AuthenticationResponse refresh(
            @RequestBody String refreshToken, HttpServletResponse response
    ) {
            return authService.handleRefresh(refreshToken);
    }

}
