package main.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtParser jwtParser;

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpirationDate(String token) {
        return  extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return  extractExpirationDate(token).before(new Date());
    }

    public String extractUsername(String token) {
        return  extractClaim(token,Claims::getSubject);
    }
}
