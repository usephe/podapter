package com.audiophileproject.main.proxies;

import org.springframework.stereotype.Service;

// TODO: use the actual service to get the space limit
@Service
public class UserProxy {
    public long getUserSpaceLimit(String userId) {
        return 10000000L;
    }
}
