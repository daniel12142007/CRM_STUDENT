package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Archive;
import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.LevelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive,Long> {

    @Query("""
            SELECT new com.kaitech.student_crm.payload.response.ArchiveResponse(
            a.id, 
            a.dateUpdate,
            a.newLevel, 
            a.oldLevel, 
            a.status,
            a.student)
            FROM Archive a
            """)
    List<ArchiveResponse> findAllResponses();

}
