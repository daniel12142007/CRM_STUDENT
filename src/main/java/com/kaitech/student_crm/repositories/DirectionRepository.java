package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {

    @Query("""
            select new com.kaitech.student_crm.payload.response.DirectionResponse(
            d.id,
            d.name,
            d.description
            )
            from Direction d
            order by d.id
            """)
    List<DirectionResponse> findAllDirections();

    @Query("""
            select new com.kaitech.student_crm.payload.response.DirectionResponse(
            d.id,
            d.name
            )
            from Direction d
            where d.id = :directorId
            """)
    DirectionResponse findByIdDirectorResponse(@Param("directorId") Long directorId);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Direction> findByName(String directionName);
}
