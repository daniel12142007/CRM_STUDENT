package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Notification;
import com.kaitech.student_crm.payload.response.NotificationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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

    List<Notification> findAllByStudentIdAndSeeFalse(@Param("studentId") Long studentId);

    @Query("""
            select new com.kaitech.student_crm.payload.response.NotificationResponse(
            n.id,
            n.message,
            n.date
            )from Notification n
            where n.student.id = :studentId
            and n.see = false
            """)
    List<NotificationResponse> findAllNewNotification(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("""
            delete from Notification n
            where n.id in :ids and n.student.id = :id
            """)
    void deleteIdsNotification(@Param(value = "ids") List<Long> ids, @Param(value = "id") Long id);
}