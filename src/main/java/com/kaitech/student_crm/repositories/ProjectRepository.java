package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
                select new com.kaitech.student_crm.payload.response.ProjectResponse(
                    p.id,
                    p.title,
                    p.description,
                    p.projectType,
                    p.startDate,
                    p.endDate
                )
                from Project p
                order by p.id
            """)
    List<ProjectResponse> findAllResponse();

    @Query("""
            select new com.kaitech.student_crm.payload.response.ProjectResponse(
            p.id,
            p.title,
            p.description,
            p.projectType,
            p.startDate,
            p.endDate
            )
            from Project p
            where p.id = :projectId
            """)
    ProjectResponse findByIdResponse(@Param("projectId") Long projectId);

    @Query("select p.title from Project p join p.students s on s.id = :studentId")
    List<String> findTitlesByStudentId(@Param(value = "studentId") Long studentId);

    boolean existsByTitle(String title);
}