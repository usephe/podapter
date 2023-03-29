package com.audiophileproject.usermanagement.services;

import com.audiophileproject.usermanagement.models.RefreshToken;
import com.audiophileproject.usermanagement.models.User;
import com.audiophileproject.usermanagement.repos.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

//    private static final String SECRET_KEY = "4125442A472D4A614E645267556B58703273357638792F423F4528482B4D6250";


    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    JwtService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private Date EXtractExpirationDate(String token) {
        return  extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return  extractClaim(token,Claims::getSubject);
    }
    private boolean isTokenExpired(String token) {
        return  EXtractExpirationDate(token).before(new Date());
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){

        // saves the private and public keys to the disk
        // you can comment it after generating the keys files
//        saveKeyPair();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*5)) // 5 minute
                .signWith(getSignInKey(), SignatureAlgorithm.RS256)
                .compact();
    }


    /**
     * create a refresh token for a specific user and store it in database
     * @param user
     * @return the refresh token instance of RefreshToken
     */
    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(60*60*24*30)); // expire after a month

        String token = UUID.randomUUID().toString();
        refreshToken.setToken(token);
        refreshToken.setExpired(false);
        refreshTokenRepository.save(refreshToken);
        return  refreshToken;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * the signing key will be the private key of the RSA keypair encryption
     * @return the PrivateKey instance
     */
    private PrivateKey getSignInKey() {
        try{
            File privateKeyFile = new File("userManagement/private.key");
            byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(privateKeySpec);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private PublicKey getPublicKey() {
        try{
            File publicKeyFile = new File("userManagement/public.key");
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(publicKeySpec);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void saveKeyPair(){
        try{
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair= generator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            try(FileOutputStream fos = new FileOutputStream("userManagement/public.key")){
                fos.write(publicKey.getEncoded());
            }catch (Exception e){
                e.printStackTrace();
            }
            try(FileOutputStream fos = new FileOutputStream("userManagement/private.key")){
                fos.write(privateKey.getEncoded());
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

    }


}
