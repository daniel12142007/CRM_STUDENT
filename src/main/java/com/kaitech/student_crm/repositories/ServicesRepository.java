package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Services;
import com.kaitech.student_crm.payload.response.ServicesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    @Query("""
            select new com.kaitech.student_crm.payload.response.ServicesResponse(
            s.id,
            s.title,
            s.description,
            s.price
            )from Services s order by s.id
            """)
    List<ServicesResponse> findAllResponse();

    @Query("""
            select new com.kaitech.student_crm.payload.response.ServicesResponse(
            s.id,
            s.title,
            s.description,
            s.price
            )from Services s where s.id = :serviceId
            """)
    Optional<ServicesResponse> findByIdResponse(@Param(value = "serviceId") Long serviceId);
}