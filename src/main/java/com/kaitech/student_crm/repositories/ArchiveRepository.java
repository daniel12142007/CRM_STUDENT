package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Archive;
import com.kaitech.student_crm.payload.response.ArchiveResponse;
import com.kaitech.student_crm.payload.response.LevelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    @Query("""
            select new com.kaitech.student_crm.payload.response.ArchiveResponse(
            a.id,
            a.dateUpdate,
            a.newLevel,
            a.oldLevel,
            a.status,
            s.id,
            s.image,
            s.firstName,
            s.lastName,
            coalesce(s.email,'Account not found')
            )from Archive a
            join a.student s
            order by a.id desc
            """)
    List<ArchiveResponse> findAllArchiveResponse();

    @Query("""
            select new com.kaitech.student_crm.payload.response.ArchiveResponse(
            a.id,
            a.dateUpdate,
            a.newLevel,
            a.oldLevel,
            a.status,
            s.id,
            s.image,
            s.firstName,
            s.lastName,
            coalesce(s.email,'Account not found')
            )from Archive a
            join a.student s
            where a.id = :archiveId
            """)
    Optional<ArchiveResponse> findByIdArchiveResponse(@Param(value = "archiveId") Long archiveId);

    @Query("""
            select new com.kaitech.student_crm.payload.response.ArchiveResponse(
            a.id,
            a.dateUpdate,
            a.newLevel,
            a.oldLevel,
            a.status,
            s.id,
            s.image,
            s.firstName,
            s.lastName,
            coalesce(s.email,'Account not found')
            )from Archive a
            join a.student s
            on s.id = :studentId
            order by a.id desc
            """)
    List<ArchiveResponse> findAllResponseByStudentId(@Param(value = "studentId") Long studentId);
}