package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Notification;
import com.kaitech.student_crm.payload.response.NotificationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("""
            select new com.kaitech.student_crm.payload.response.NotificationResponse(
            n.id,
            n.message,
            n.date
            )from Notification n
            where n.student.id = :studentId
            """)
    List<NotificationResponse> findAllResponseByStudentId(@Param("studentId") Long studentId);
}