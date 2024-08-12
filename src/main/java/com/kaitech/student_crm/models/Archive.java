package com.kaitech.student_crm.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    private LocalDate dateUpdate;
    private String newLevel;
    private String oldLevel;
    private Integer oldPoint;
    private Integer newPoint;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToMany(mappedBy = "archive")
    private List<Notification> notifications;
}