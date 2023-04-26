package com.podapter.main.services;

import com.podapter.main.config.props.StorageProperties;
import com.podapter.main.exceptions.StorageException;
import com.podapter.main.exceptions.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService {

    private final Path rootLocation;
    public FileSystemStorageService(StorageProperties properties) {
        rootLocation = Paths.get(properties.getLocation());
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public void store(MultipartFile file, Path location) {
        try {
            if (file.isEmpty())
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());

            Path fileLocation = load(location);
            Files.createDirectories(fileLocation.getParent());
            Files.copy(file.getInputStream(), fileLocation);

        } catch (IOException e) {
            throw new StorageException("failed to store file: `" + file.getOriginalFilename() + "`", e);
        }
    }

    public Path load(String filePath) {
        return load(Paths.get(filePath));
    }

    public Path load(Path filePath) {
        var resolved =  rootLocation.resolve(filePath).normalize();
        if (!resolved.startsWith(rootLocation))
            throw new StorageException("invalid path: " + filePath);
        return resolved;
    }

    public Stream<Path> loadAll(String location) {
        return loadAll(Paths.get(location));
    }

    public Stream<Path> loadAll(Path location) {
        Path dirLocation = load(location);
        try (var walk = Files.walk(dirLocation, 1)){
            return walk
                    .filter(path -> !path.equals(dirLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    public Resource loadAsResource(Path location) {
        try {
            Path file = load(location);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + file.getFileName());

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + location.getFileName(), e);
        }
    }

    public void deleteAll(Path dirPath) {
        Path path = load(dirPath);
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    public boolean delete(Path filePath) {
        File file = load(filePath).toFile();
        return file.delete();
    }
}
