package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Archive;
import com.kaitech.student_crm.models.Level;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.LevelResponse;
import com.kaitech.student_crm.repositories.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository repo;

}
