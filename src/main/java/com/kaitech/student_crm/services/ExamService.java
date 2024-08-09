package com.kaitech.student_crm.services;
import com.kaitech.student_crm.repositories.ExamRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final StudentUserRepository studentRepository;

}
