package com.audiophileproject.usermanagement.services;

import com.audiophileproject.usermanagement.exceptions.InvalidTokenException;
import com.audiophileproject.usermanagement.exceptions.TokenExpiredException;
import com.audiophileproject.usermanagement.models.*;
import com.audiophileproject.usermanagement.repos.RefreshTokenRepository;
import com.audiophileproject.usermanagement.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    // TODO: validate the data before saving it
    public AuthenticationResponse register(RegisterRequest request){



        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(ERole.ROLE_USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .authToken(jwtToken)
                .refreshToken(jwtService.createRefreshToken(user).getToken())
                .build();
    }

    /**
     * authenticate the user by his username and password
     * @param request
     * @return AuthenticationResponse
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .authToken(jwtToken)
                .refreshToken(jwtService.createRefreshToken(user).getToken())
                .build();
    }


    /**
     * This method will be responsible for validating refresh token, and if valid, return a new jwt token
     * @param request
     * @return AuthenticationResponse
     *
     */
    public AuthenticationResponse handleRefresh(String token)  {
        RefreshToken  refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->{
            throw new InvalidTokenException();
        });
        if(refreshToken.isExpired() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw  new TokenExpiredException();
        }
        else{
            String jwtToken = jwtService.generateToken(refreshToken.getUser());
            return AuthenticationResponse.builder()
                    .authToken(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .build();
        }
    }

}
