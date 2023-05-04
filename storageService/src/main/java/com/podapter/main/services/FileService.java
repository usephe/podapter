package com.podapter.main.services;

import com.podapter.main.exceptions.StorageFileNotFoundException;
import com.podapter.main.models.FileMetadata;
import com.podapter.main.repositories.FileMetadataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileSystemStorageService storageService;
    private final static Logger logger = Logger.getLogger(FileService.class.getName());

    public FileMetadata save(MultipartFile file, String userId) {
        var storedFile = FileMetadata.builder()
                .originalFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .storedFileName(UUID.randomUUID().toString())
                .userId(userId)
                .build();

        storageService.store(file, storedFile.getFilePath());
        var savedFile = fileMetadataRepository.save(storedFile);

        logger.info("file uploaded successfully: " + storedFile);

        return savedFile;
    }

    public FileMetadata findById(Long id, String userId) {
        return fileMetadataRepository.findByIdAndUserId(id, userId).orElseThrow();
    }

    public List<FileMetadata> findAll(String userId) {
        return fileMetadataRepository.findAllByUserId(userId);
    }

    public void deleteById(Long id, String userId) {
        var fileMetadata = fileMetadataRepository.findByIdAndUserId(id, userId).orElseThrow();

        try {
            storageService.delete(fileMetadata.getFilePath());
        } catch (NoSuchFileException e) {
            logger.warning("File does not exist: " +  e.getMessage() + ": " + fileMetadata);
        } catch (IOException e) {
            logger.warning("Failed to delete file: " +  e.getMessage() + ": " + fileMetadata);
            throw new RuntimeException("Failed to delete file: " + fileMetadata.getOriginalFileName());
        }

        fileMetadataRepository.delete(fileMetadata);
    }

    @Transactional
    public void deleteAll(String userId) {
        storageService.deleteAll(Paths.get(userId));
        fileMetadataRepository.deleteAllByUserId(userId);
    }

    public Path loadById(Long id, String userId) throws IOException {
        FileMetadata fileMetadata = fileMetadataRepository.findByIdAndUserId(id, userId).orElseThrow();
        return storageService.load(fileMetadata.getFilePath());
    }

    public Resource loadAsResourceById(Long id, String userId) {
        FileMetadata fileMetadata = fileMetadataRepository.findByIdAndUserId(id, userId).orElseThrow();
        try {
            return storageService.loadAsResource(fileMetadata.getFilePath());
        } catch (StorageFileNotFoundException ex) {
            logger.warning("File does not exist: " +  ex.getMessage() + ": " + fileMetadata);
            fileMetadataRepository.delete(fileMetadata);
            throw new NoSuchElementException(ex.getMessage());
        }
    }

    public long getTotalSpaceUsage(String userId) {
        List<FileMetadata> fileMetadataList = fileMetadataRepository.findAllByUserId(userId);
        long totalSize = 0;
        for (var fileMetadata: fileMetadataList) {
           totalSize += fileMetadata.getFileSize();
        }

        return totalSize;
    }
}
