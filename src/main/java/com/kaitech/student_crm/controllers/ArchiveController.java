package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.services.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/archive")
public class ArchiveController {
    private final ArchiveService service;

}
