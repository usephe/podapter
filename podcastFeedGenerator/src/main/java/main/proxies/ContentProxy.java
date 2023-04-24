package main.proxies;

import main.dto.ContentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "content-management")
public interface ContentProxy {
    @GetMapping("/api/v1/content")
    List<ContentDTO> getAllContent(@RequestHeader("userId") String userId);
}
