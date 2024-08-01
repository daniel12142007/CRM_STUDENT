package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Weeksday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeksdayRepository extends JpaRepository<Weeksday, Long> {
}
