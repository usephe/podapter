package com.podapter.main.controllers;

import com.podapter.main.dto.FileDTOMapper;
import com.podapter.main.dto.SpaceUsageDTO;
import com.podapter.main.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/usage")
public class UsageController {
    private final FileService fileService;
    private final FileDTOMapper fileDTOMapper;
    private final static Logger logger =
            Logger.getLogger(UsageController.class.getName());

    @GetMapping("/{userId}")
    public SpaceUsageDTO getSpaceUsage(@PathVariable String userId) {
        var totalSpaceUsage = fileService.getTotalSpaceUsage(userId);
        return SpaceUsageDTO.builder()
                .usedSpace(totalSpaceUsage)
                .build();
    }
}
