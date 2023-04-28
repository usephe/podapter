package com.podapter.main.dto;

import com.podapter.main.models.FileMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

@Component
public class FileDTOMapper implements Function<FileMetadata, FileDTO> {
    @Value("${storage.url}")
    private String storageUrl;
    @Override
    public FileDTO apply(FileMetadata fileMetadata) {
        try {
            return FileDTO.builder()
                    .id(fileMetadata.getId())
                    .fileName(fileMetadata.getOriginalFileName())
                    .fileSize(fileMetadata.getFileSize())
                    .fileType(fileMetadata.getFileType())
                    .url(new URL(storageUrl +  "/storage/file/" + fileMetadata.getUserId()  + "/" + fileMetadata.getId()))
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
