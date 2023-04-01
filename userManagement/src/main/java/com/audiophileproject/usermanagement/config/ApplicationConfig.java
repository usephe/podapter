package com.audiophileproject.usermanagement.config;

import com.audiophileproject.usermanagement.repos.UserRepository;
import com.audiophileproject.usermanagement.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileOutputStream;
import java.security.*;

@Configuration
@RequiredArgsConstructor
@EnableDiscoveryClient
public class ApplicationConfig {
    private  final UserRepository userRepository;
    private final UserService userDetailsService;
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws  Exception {
        return config.getAuthenticationManager();
    }


    @PostConstruct
    private void saveKeyPair(){
        try{
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair= generator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            try{
                FileOutputStream fos1 = new FileOutputStream("api-gateway/public.key");
                FileOutputStream fos2 = new FileOutputStream("userManagement/public.key");
                fos1.write(publicKey.getEncoded());
                fos2.write(publicKey.getEncoded());
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
