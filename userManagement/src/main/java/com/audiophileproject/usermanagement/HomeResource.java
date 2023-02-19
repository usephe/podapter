package com.audiophileproject.usermanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home(){
        return "Welcome Home!";
    }


    @GetMapping("/user")
    public String user(){
        return "for users only";
    }


    @GetMapping("/admin")
    public String admin(){
        return "for admins only";
    }
    
}
