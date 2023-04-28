package com.audiophileproject.main.repositories;

import com.audiophileproject.main.models.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findByContentId(Long id);
}
