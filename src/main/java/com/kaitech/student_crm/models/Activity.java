package com.kaitech.student_crm.models;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    public Activity() {
    }

}
