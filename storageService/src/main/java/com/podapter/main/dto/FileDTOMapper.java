package com.podapter.main.dto;

import com.podapter.main.models.FileMetadata;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FileDTOMapper implements Function<FileMetadata, FileDTO> {
    @Override
    public FileDTO apply(FileMetadata fileMetadata) {
        return FileDTO.builder()
                .id(fileMetadata.getId())
                .fileName(fileMetadata.getOriginalFileName())
                .fileSize(fileMetadata.getFileSize())
                .fileType(fileMetadata.getFileType())
                .build();
    }
}
