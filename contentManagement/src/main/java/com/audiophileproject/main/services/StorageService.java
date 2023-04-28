package com.audiophileproject.main.services;

import com.audiophileproject.main.dto.FileDTO;
import com.audiophileproject.main.models.Content;
import com.audiophileproject.main.models.FileMetadata;
import com.audiophileproject.main.proxies.StorageProxy;
import com.audiophileproject.main.repositories.FileMetadataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class StorageService {
    private final StorageProxy storageProxy;
    private final FileMetadataRepository fileMetadataRepository;

    public FileMetadata save(MultipartFile file, String userId) {
        var fileDTO = storageProxy.store(file, userId);
        var fileMetadata = FileMetadata.builder()
                .storageId(fileDTO.id())
                .fileType(fileDTO.fileType())
                .fileSize(fileDTO.fileSize())
                .fileName(fileDTO.fileName())
                .url(fileDTO.url())
                .build();
        return fileMetadataRepository.save(fileMetadata);
    }

    public FileMetadata save(FileMetadata fileMetadata) {
        return fileMetadataRepository.save(fileMetadata);
    }

    public FileMetadata findById(Long id) {
        return fileMetadataRepository.findById(id).orElseThrow();
    }

    public FileMetadata findByContent(Content content)  {
        return findByContentId(content.getId());
    }

    public FileMetadata findByContentId(Long contentId)  {
        return fileMetadataRepository.findByContentId(contentId).orElseThrow();
    }

    public void deleteById(Long id) {
        delete(fileMetadataRepository.findById(id).orElseThrow());
    }

    public void deleteByContentId(Long contentId) {
        delete(fileMetadataRepository.findByContentId(contentId).orElseThrow());
    }

    public void delete(FileMetadata fileMetadata) {
        storageProxy.deleteById(fileMetadata.getStorageId(), fileMetadata.getContent().getUserId());
        fileMetadataRepository.delete(fileMetadata);
    }
}
