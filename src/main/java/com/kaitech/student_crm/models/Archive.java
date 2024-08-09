package com.kaitech.student_crm.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "archive")
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateUpdate;
    private String newLevel;
    private String oldLevel;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToMany(mappedBy = "archive")
    private List<Notification> notifications;
    private String image;
    private String firstName;
    private String lastName;
}
