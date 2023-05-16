package com.audiophileproject.usermanagement.controllers;

import com.audiophileproject.usermanagement.dto.UserUpdateDTO;
import com.audiophileproject.usermanagement.models.User;
import com.audiophileproject.usermanagement.models.UserResponse;
import com.audiophileproject.usermanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public UserResponse getCurrentUser(){
     User currUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

     return UserResponse.builder()
             .email(currUser.getEmail())
             .username(currUser.getUsername())
//             .firstname(currUser.getFirstname())
//             .lastname(currUser.getLastname())
             .build();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserDetails(@PathVariable String userId) {
        var user = userService.loadUserByUserId(userId);
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
//                .firstname(user.getFirstname())
//                .lastname(user.getLastname())
                .build();
    }

    @PutMapping
    public UserResponse updateUser(@RequestBody UserUpdateDTO user){
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("ENCODED PASSWORD "+user.getPassword());
        User newUser = userService.updateUser(user);

        return UserResponse.builder()
                .email(newUser.getEmail())
                .username(newUser.getUsername())
                .build();
    }

    // THIS HAS TO BE FOR ADMINS ONLY
    @DeleteMapping("/{username}")
    public String deleteUser(@RequestParam String username){
        userService.deleteUser(username);
        return "user deleted successfully";
    }
    @DeleteMapping("")
    public String deleteAccount(@RequestParam String username){
        User currUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteUser(currUser.getUsername());
        return "account deleted successfully";
    }

}
