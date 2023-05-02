package com.audiophileproject.proxies;

import com.audiophileproject.dto.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management")
public interface UserProxy {
    @GetMapping("/api/v1/user/{userId}")
    UserDetailsDTO getUserDetailsByUserId(@PathVariable("userId") String userId);
}
