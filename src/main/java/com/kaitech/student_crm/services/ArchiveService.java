package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Archive;
import com.kaitech.student_crm.models.Level;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.repositories.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {
    private final ArchiveRepository archiveRepository;

    public List<ArchiveResponse> findAllArchive() {
        log.info("Получение всех ArchiveResponse.");
        return archiveRepository.findAllArchiveResponse();
    }

    public List<ArchiveResponse> findAllByStudentId(Long studentId) {
        log.info("Получение всех ArchiveResponse по studentId: {}", studentId);
        return archiveRepository.findAllResponseByStudentId(studentId);
    }

    public ArchiveResponse findById(Long archiveId) {
        log.info("Поиск ArchiveResponse по archiveId: {}", archiveId);
        ArchiveResponse archiveResponse = archiveRepository.findByIdArchiveResponse(archiveId)
                .orElseThrow(() -> {
                    log.error("ArchiveResponse с archiveId: {} не найден.", archiveId);
                    return new NotFoundException("Not found archive ID: " + archiveId);
                });
        log.info("Найден ArchiveResponse с archiveId: {}", archiveId);
        return archiveResponse;
    }

    public MessageResponse deleteById(Long archiveId) {
        log.info("Удаление Archive по archiveId: {}", archiveId);
        Archive archive = archiveRepository.findById(archiveId).orElseThrow(() -> {
            log.error("Archive с archiveId: {} не найден.", archiveId);
            return new NotFoundException("Not found archive ID: " + archiveId);
        });
        archiveRepository.delete(archive);
        log.info("Archive с archiveId: {} успешно удален.", archiveId);
        return new MessageResponse("Successfully removed");
    }
}