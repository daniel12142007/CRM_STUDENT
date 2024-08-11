package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.request.LevelRequest;
import com.kaitech.student_crm.payload.response.LevelResponse;
import com.kaitech.student_crm.services.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/level")
public class LevelController {
    private final LevelService levelService;

    @Autowired
    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("find/by/id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "возвращает level по ID")
    public LevelResponse findById(@PathVariable Long id) {
        return levelService.findById(id);
    }

    @GetMapping("find/by/title/{title}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "возвращает level по title")
    public LevelResponse findByTitle(@PathVariable String title) {
        return levelService.findByTitle(title);
    }

    @GetMapping("find/all")
    @Operation(summary = "возвращает все level, сортируя их по pointFrom по возврастанию")
    public List<LevelResponse> findAll() {
        return levelService.findAll();
    }

}