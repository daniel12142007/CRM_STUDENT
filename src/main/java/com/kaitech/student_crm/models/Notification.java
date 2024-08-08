package com.kaitech.student_crm.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    @ManyToOne
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


}
