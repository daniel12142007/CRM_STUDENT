package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.services.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/exam")
public class ExamController {
    private final ExamService examService;

}
