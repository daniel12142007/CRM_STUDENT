package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityRepository  extends JpaRepository<Activity, Long> {
}
