package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.ArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/archive")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class ArchiveController {
    private final ArchiveService archiveService;

    @GetMapping
    @Operation(summary = "Получить все архивы", description = "Возвращает список всех архивов.")
    public ResponseEntity<List<ArchiveResponse>> getAllArchives() {
        return ResponseEntity.ok(archiveService.findAllArchive());
    }

    @GetMapping("/students/archives/{studentId}")
    @Operation(summary = "Получить архивы по студенту", description = "Возвращает список архивов, связанных с указанным studentId.")
    public ResponseEntity<List<ArchiveResponse>> getArchivesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(archiveService.findAllByStudentId(studentId));
    }

    @GetMapping("/{archiveId}")
    @Operation(summary = "Получить архив по ID", description = "Возвращает архив, соответствующий указанному archiveId.")
    public ResponseEntity<ArchiveResponse> getArchiveById(@PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.findById(archiveId));
    }

    @DeleteMapping("delete/by/{archiveId}")
    @Operation(summary = "Удалить архив по ID", description = "Удаляет архив, соответствующий указанному archiveId.")
    public ResponseEntity<MessageResponse> deleteArchiveById(@PathVariable Long archiveId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(archiveService.deleteById(archiveId));
    }
}