package com.podapter.main.repositories;

import com.podapter.main.models.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findAllByUserId(String userId);
    Optional<FileMetadata> findByIdAndUserId(Long id, String userId);
    long deleteByIdAndUserId(Long id, String userId);
    void deleteAllByUserId(String userId);
}
