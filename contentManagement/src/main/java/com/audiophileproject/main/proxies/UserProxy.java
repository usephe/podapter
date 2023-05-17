package com.audiophileproject.main.proxies;

import com.audiophileproject.main.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service")
public interface UserProxy {
    @GetMapping("/api/v1/payment/limit")
    UserDTO getUserSpaceLimit(@RequestParam("userId") String userId);
}
