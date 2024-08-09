package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Archive;
import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.repositories.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {
    private final ArchiveRepository archiveRepository;

    public List<ArchiveResponse> findAllArchive() {
        return archiveRepository.findAllArchiveResponse();
    }

    public List<ArchiveResponse> findAllByStudentId(Long studentId) {
        return archiveRepository.findAllResponseByStudentId(studentId);
    }

    public ArchiveResponse findById(Long archiveId) {
        return archiveRepository.findByIdArchiveResponse(archiveId).orElseThrow(
                () -> new NotFoundException("Not found archive ID: " + archiveId)
        );
    }

    public MessageResponse deleteById(Long archiveId) {
        Archive archive = archiveRepository.findById(archiveId).orElseThrow(
                () -> new NotFoundException("Not found archive ID: " + archiveId)
        );
        archiveRepository.delete(archive);
        return new MessageResponse("Successfully removed");
    }
}