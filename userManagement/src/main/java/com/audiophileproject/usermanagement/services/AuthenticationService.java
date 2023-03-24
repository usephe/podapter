package com.audiophileproject.usermanagement.services;

import com.audiophileproject.usermanagement.models.*;
import com.audiophileproject.usermanagement.repos.RefreshTokenRepository;
import com.audiophileproject.usermanagement.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
     * authenticate the user by his email and password
     * @param request
     * @return
     * @apiNote we authenticate by email and not username
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
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
    // TODO complete this method
    public AuthenticationResponse handleRefresh(String token) throws Exception {
        RefreshToken  refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->{
            return  new Exception("Token Not Valid");
        });
        if(refreshToken.isExpired() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw  new Exception("Token Expired");
        }

        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        return null;
    }
}
