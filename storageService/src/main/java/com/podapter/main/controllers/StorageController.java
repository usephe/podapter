package com.podapter.main.controllers;

import com.podapter.main.dto.FileDTO;
import com.podapter.main.dto.FileDTOMapper;
import com.podapter.main.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@RestController
@RequestMapping("/storage")
public class StorageController {
    private final FileService fileService;
    private final FileDTOMapper fileDTOMapper;
    private final static Logger logger =
            Logger.getLogger(StorageController.class.getName());

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileDTO handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestHeader String userId
    ) {
        var storedFile = fileService.save(file, userId);
        return fileDTOMapper.apply(storedFile);
    }

    @GetMapping
    public List<FileDTO> listUploadedFiles(@RequestHeader String userId) {
        logger.info("Listing all uploaded files for userid: " + userId);
        return fileService.findAll(userId)
                .stream()
                .map(fileDTOMapper)
                .toList();
    }

    @GetMapping("/{id}")
    public FileDTO listUploadedFile(
            @PathVariable Long id,
            @RequestHeader String userId
    ) {
        return fileDTOMapper.apply(fileService.findById(id, userId));
    }

    @GetMapping("/file/{userId}/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @PathVariable Long id,
            @PathVariable String userId
    ) {

        Resource file = fileService.loadAsResourceById(id, userId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable Long id,
            @RequestHeader String userId
    ) {
        fileService.deleteById(id, userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(
            @RequestHeader String userId
    ) {
        fileService.deleteAll(userId);
    }
}
