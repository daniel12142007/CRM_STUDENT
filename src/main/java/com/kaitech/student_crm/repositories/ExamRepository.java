package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Exam;
import com.kaitech.student_crm.payload.response.ExamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {


}
