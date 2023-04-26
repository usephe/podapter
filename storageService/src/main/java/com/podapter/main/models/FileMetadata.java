package com.podapter.main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String originalFileName;
    @NotNull
    @Column(nullable = false)
    private Long fileSize;
    @NotNull
    private String fileType;
    @NotNull
    @Column(nullable = false)
    private String storedFileName;
    @NotNull
    @Column(nullable = false)
    private String userId;

    public Path getFilePath() {
        return Paths.get(userId, storedFileName);
    }
}
