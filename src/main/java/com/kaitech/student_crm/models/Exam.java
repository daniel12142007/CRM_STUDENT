package com.kaitech.student_crm.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateStart;
    private LocalDate datEnd;
    private String newLevel;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
