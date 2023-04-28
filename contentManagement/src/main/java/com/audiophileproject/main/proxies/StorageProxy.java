package com.audiophileproject.main.proxies;

import com.audiophileproject.main.dto.FileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "storage-service")
public interface StorageProxy {
    @PostMapping(value = "/storage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileDTO store(
            @RequestPart("file") MultipartFile file,
            @RequestHeader("userId") String userId
    );
    @GetMapping("/storage")
    List<FileDTO> getAllLocalFiles(@RequestHeader("userId") String userId);

    @DeleteMapping("/storage/{id}")
    void deleteById(
            @PathVariable("id") Long id,
            @RequestHeader("userId") String userId
    );
}
