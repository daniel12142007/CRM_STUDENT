package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.response.NotificationResponse;
import com.kaitech.student_crm.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Получение своих уведомлений ")
    @GetMapping("my/notification")
    public List<NotificationResponse> myNotification() throws AccessDeniedException {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return notificationService.myNotification(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Operation(summary = "Получение своих не прочитанных уведомлений")
    @GetMapping("my/new/notification")
    public List<NotificationResponse> findNewNotification() throws AccessDeniedException {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return notificationService.findAllNewNotification(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Operation(summary = "Удаление уведомлений")
    @DeleteMapping("delete/by/{ids}")
    public String deleteByIdsNotification(@PathVariable List<Long> ids) throws AccessDeniedException {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        notificationService.deleteByIdsNotification(ids, SecurityContextHolder.getContext().getAuthentication().getName());
        return "Успешно удалено";
    }
}