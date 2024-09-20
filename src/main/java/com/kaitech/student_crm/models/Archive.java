package com.kaitech.student_crm.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "archive")
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String firstName;
    private String lastName;
    private LocalDateTime dateUpdate;
    private String newLevel;
    private String oldLevel;
    private Integer newPoint;
    private Integer oldPoint;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Archive(Student student, Level level) {
        this.student = student;
        this.image = student.getImage();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.newLevel = level.getTitle();
        this.oldLevel = level.getTitle();
        this.newPoint = student.getPoint();
        this.oldPoint = student.getPoint();
        this.dateUpdate = LocalDateTime.now();
    }

    public Archive(Student newStudent, Integer oldPoint, Level newLevel, Level oldLevel) {
        this.student = newStudent;
        this.image = newStudent.getImage();
        this.firstName = newStudent.getFirstName();
        this.lastName = newStudent.getLastName();
        this.newLevel = newLevel.getTitle();
        this.oldLevel = oldLevel.getTitle();
        this.newPoint = newStudent.getPoint();
        this.oldPoint = oldPoint;
        this.dateUpdate = LocalDateTime.now();
    }
}