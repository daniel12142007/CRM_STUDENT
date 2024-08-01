package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.ServiceItem;
import com.kaitech.student_crm.payload.response.ServiceItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    @Query("""
            select new com.kaitech.student_crm.payload.response.ServiceItemResponse(
            item.id,
            item.title,
            item.description,
            item.services.id
            )from ServiceItem item
            where item.id = :itemId
            """)
    Optional<ServiceItemResponse> findByIdResponse(@Param(value = "itemId") Long itemId); @Query("""
            select new com.kaitech.student_crm.payload.response.ServiceItemResponse(
            item.id,
            item.title,
            item.description,
            item.services.id
            )from ServiceItem item
            where item.services.id = :serviceId
            """)
    List<ServiceItemResponse> findAllByServiceId(@Param(value = "serviceId") Long serviceId);

    @Query("""
            select new com.kaitech.student_crm.payload.response.ServiceItemResponse(
            item.id,
            item.title,
            item.description,
            item.services.id
            )from ServiceItem item
            order by item.id
            """)
    List<ServiceItemResponse> findAllResponse();
}