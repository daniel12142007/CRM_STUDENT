package com.kaitech.student_crm.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@NoArgsConstructor
@Entity
@Table(name = "levels")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer pointFrom;
    private Integer pointTo;
    @Column(length = 900)
    private String description;

    public Level(String title, Integer pointFrom, Integer pointTo, String description) {
        this.title = title;
        this.pointFrom = pointFrom;
        this.pointTo = pointTo;
        this.description = description;
    }
}