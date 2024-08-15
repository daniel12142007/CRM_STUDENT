package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Level;
import com.kaitech.student_crm.payload.response.LevelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    @Query("""
            select new com.kaitech.student_crm.payload.response.LevelResponse(
            l.id,
            l.title,
            l.description,
            l.pointFrom,
            l.pointTo
            )from Level l
            where l.id = :levelId
            """)
    Optional<LevelResponse> findByIdResponse(@Param(value = "levelId") Long levelId);

    @Query("""
            select new com.kaitech.student_crm.payload.response.LevelResponse(
            l.id,
            l.title,
            l.description,
            l.pointFrom,
            l.pointTo
            )from Level l
            where l.title = :title
            """)
    Optional<LevelResponse> findByTitleResponse(@Param(value = "title") String title);

    @Query("""
            select new com.kaitech.student_crm.payload.response.LevelResponse(
            l.id,
            l.title,
            l.description,
            l.pointFrom,
            l.pointTo
            )from Level l
            order by l.pointFrom
            """)
    List<LevelResponse> findAllResponse();

    @Query("select concat('[title: ', l.title, ', pointFrom: ', l.pointFrom, ', pointTo: ', l.pointTo, ']') from Level l where :points between l.pointFrom and (l.pointTo-1)")
    List<String> checkPoints(@Param(value = "points") Integer points);

    @Query("select concat('[title: ', l.title, ', pointFrom: ', l.pointFrom, ', pointTo: ', l.pointTo, ']') from Level l where :points between l.pointFrom and (l.pointTo-1) and not l.id = :levelId")
    List<String> checkPoints(@Param(value = "points") Integer points, @Param(value = "levelId") Long levelId);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long levelId);

    @Query("select l from Level l where :point between l.pointFrom and (l.pointTo - 1)")
    Optional<Level> findBetweenPointFrontAndPointTo(@Param("point") Integer point);

    @Query("""
            select coalesce(
            case when :point >= (select max(l1.pointTo) from Level l1)
            then l2 else null end,null
            ) from Level l2 where l2.pointTo = (select max(l3.pointTo) from Level l3)
            """)
    Level findLevelIfNull(@Param(value = "point") Integer point);
}