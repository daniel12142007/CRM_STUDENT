package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.payload.response.NotificationResponse;
import com.kaitech.student_crm.repositories.NotificationRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import com.kaitech.student_crm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final StudentUserRepository studentRepository;

    public List<NotificationResponse> findAllByStudentId(Long studentId, String email) {
        log.info("Запрос на получение уведомлений для студента с ID: {} и email: {}", studentId, email);
        if (!userRepository.findUserByEmail(email).orElseThrow(
                () -> {
                    log.error("Пользователь с email: {} не найден", email);
                    return new NotFoundException("Not found user email: " + email);
                }
        ).getRole().equals(ERole.ROLE_ADMIN)
            && !studentRepository.findById(studentId).orElseThrow(
                () -> {
                    log.error("Студент с ID: {} не найден", studentId);
                    return new NotFoundException("Not found student ID: " + studentId);
                }
        ).getEmail().equals(email)) {
            log.error("Отказано в доступе. Пользователь с email: {} не имеет доступа к уведомлениям студента с ID: {}", email, studentId);
            throw new AccessDeniedException("You do not have access to get notification this account, you must be an admin or the owner of this account");
        }
        return notificationRepository.findAllResponseByStudentId(studentId);
    }
}