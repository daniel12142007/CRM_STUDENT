package com.kaitech.student_crm.controllers;


import com.kaitech.student_crm.services.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {
    private final S3FileService appService;

    @GetMapping("/files")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> listFiles() {
        return appService.listFiles();
    }

    @GetMapping("video/{filename}")
    public ResponseEntity<InputStreamResource> viewPdf(@PathVariable String filename) {
        return appService.viewVideo(filename);
    }

    @GetMapping("image/{filename}")
    public ResponseEntity<InputStreamResource> viewImage(@PathVariable String filename) throws IOException {
        return appService.viewImage(filename);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        return appService.upload(file);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Map<String, String> delete(@RequestParam String fileName) {
        return appService.delete(fileName);
    }
}