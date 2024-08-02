package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.StudentDTOForAll;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.payload.response.StudentResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentUserRepository extends JpaRepository<Student, Long> {
    Optional<Student> findUserById(Long id);

    boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);

    @Query("""
            select
            new com.kaitech.student_crm.dtos.StudentDTO(
            u.id,
            u.firstName,
            u.lastName,
            u.email,
            u.phoneNumber
            )
            from Student u
            where u.user.role = :role
            order by u.id
            """)
    List<StudentDTO> findAllStudentDTOByRole(@Param("role") ERole role);

    @Query("""
                        select
                        new com.kaitech.student_crm.dtos.StudentDTO(
                        u.id,
                        u.firstName,
                        u.lastName,
                        u.email,
                        u.phoneNumber
                        )
                        from Student  u
                        where u.direction.id = :directorId
                        order by u.id
            """)
    List<StudentDTO> findAllByDirectorId(@Param("directorId") Long directorId);

    @Query("""
                        select
                        new com.kaitech.student_crm.payload.response.StudentResponse(
                        st.id,
                        st.image,
                        st.firstName,
                        st.lastName,
                        st.email
                        )
                        from Project pr
                        join pr.students st
                        where pr.id = :projectId
                        order by st.id
            """)
    List<StudentResponse> findAllByProjectIdResponse(@Param("projectId") Long projectId);

    @Query("""
            select new com.kaitech.student_crm.payload.response.StudentResponse(
            s.id,
            s.image,
            s.firstName,
            s.lastName,
            s.email,
            s.phoneNumber,
            s.direction.name,
            s.status
            )from Student s
            where s.id = :studentId
            """)
    StudentResponse findByIdResponse(@Param(value = "studentId") Long studentId);

    @Query("""
            select new com.kaitech.student_crm.payload.response.StudentResponse(
            s.id,
            s.image,
            s.firstName,
            s.lastName,
            s.email,
            s.phoneNumber,
            s.direction.name,
            s.status
            )from Student s
            order by s.id
            """)
    List<StudentResponse> findAllResponse();

    @Query("""
            select new com.kaitech.student_crm.dtos.StudentDTOForAll(
            s.id,
            s.image,
            s.lastName,
            s.firstName,
            s.direction.name,
            s.status
            ) from Student s
            order by s.id
            """)
    List<StudentDTOForAll> findAllStudentDTOs();


    @Query("""
                select new com.kaitech.student_crm.payload.response.StudentResponse(
                s.id,
                s.image,
                s.firstName,
                s.lastName,
                s.email
                )
                from Student s
                join s.projects pr
                where pr.id = :projectId
            """)
    List<StudentResponse> findAllByProjectId(@Param(value = "projectId") Long projectId);

    @Query("""
             select new com.kaitech.student_crm.dtos.StudentDTO(
             s.id,
             s.image,
             s.firstName,
             s.lastName,
             s.email,
             s.phoneNumber,
             s.direction.name,
             s.status,
            (select l.title from Level l where s.point between l.pointFrom and (l.pointTo - 1)),
             s.point
             )
             from Student s
             where s.id = :studentId
             """)
    Optional<StudentDTO> findByIdStudentDTO(@Param(value = "studentId") Long studentId);

    @Query("""
            select coalesce(
            case when :point >= (select max(l.pointTo) from Level l)
            then (select l.title
            from Level l
            where l.pointTo = (select max(l2.pointTo) from Level l2)) else null end,
            null ) from Student s  where s.id = :studentId
            """)
    String findLevelIfNull(@Param("point") Integer point, @Param("studentId") Long studentId);

}