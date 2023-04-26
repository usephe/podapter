package com.podapter.main.services;

import com.podapter.main.models.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();
    FileMetadata store(MultipartFile file, String userId);
    Stream<Path> loadAll(String userId);
    Path load(String filename, String userId) throws IOException;
    Resource loadAsResource(String filename, String userId);
    void deleteAll(String userId);
}
