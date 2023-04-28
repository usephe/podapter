package com.audiophileproject.main.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "content_id")
    private Content content;
    @NotNull
    private Long storageId;
    @NotNull
    @Column(nullable = false)
    private String fileName;
    @NotNull
    @Column(nullable = false)
    private Long fileSize;
    @NotNull
    private String fileType;
    @NotNull
    private URL url;
}
