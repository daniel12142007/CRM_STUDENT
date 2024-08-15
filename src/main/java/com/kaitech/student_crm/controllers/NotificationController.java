package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.response.NotificationResponse;
import com.kaitech.student_crm.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Получение уведомлений по ID Студента ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("notifications/{studentId}")
    public List<NotificationResponse> findAllNotificationByStudent(@PathVariable Long studentId) throws AccessDeniedException {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return notificationService.findAllByStudentId(studentId, SecurityContextHolder.getContext().getAuthentication().getName());
    }
}