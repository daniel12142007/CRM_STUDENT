package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/archive")
public class ArchiveController {
    private final ArchiveService archiveService;

    @GetMapping
    public ResponseEntity<List<ArchiveResponse>> getAllArchives() {
        return ResponseEntity.ok(archiveService.findAllArchive());
    }

    @GetMapping("/students/archives/{studentId}")
    public ResponseEntity<List<ArchiveResponse>> getArchivesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(archiveService.findAllByStudentId(studentId));
    }

    @GetMapping("/{archiveId}")
    public ResponseEntity<ArchiveResponse> getArchiveById(@PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.findById(archiveId));
    }

    @DeleteMapping("delete/by/{archiveId}")
    public ResponseEntity<MessageResponse> deleteArchiveById(@PathVariable Long archiveId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(archiveService.deleteById(archiveId));
    }
}