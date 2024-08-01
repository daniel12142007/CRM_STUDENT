package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.payload.request.DirectionCreateRequest;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.DirectionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/directions")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DirectionController {

    @Autowired
    private DirectionService directionService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "Получение всех направлений с информацией о студентах")
    public ResponseEntity<List<DirectionResponse>> getAllDirectionsWithStudents() {
        List<DirectionResponse> directions = directionService.getAllDirections();
        if (directions.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(directions);
    }

    @GetMapping("/{directionId}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "Получение направления по id")
    public ResponseEntity<DirectionResponse> getDirectorWithStudents(@PathVariable Long directionId) {
        DirectionResponse response = directionService.getById(directionId);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создание нового направления")
    public ResponseEntity<DirectionResponse> createDirection(@RequestBody @Valid DirectionCreateRequest directionCreateRequest) {
        DirectionResponse createdDirection = directionService.createDirection(directionCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDirection);
    }

    @PutMapping("/{directionId}/assignStudent/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Назначение студента на направление")
    public ResponseEntity<MessageResponse> assignStudentToDirection(
            @PathVariable Long directionId,
            @PathVariable Long studentId
    ) {
        Direction direction = directionService.assignStudentToDirection(directionId, studentId);
        return ResponseEntity.ok(new MessageResponse("Assigned successfully"));
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удаление направления")
    public ResponseEntity<MessageResponse> deleteDirection(@PathVariable Long id) {
        directionService.deleteDirection(id);
        return ResponseEntity.ok(new MessageResponse("Direction was deleted"));
    }
}
