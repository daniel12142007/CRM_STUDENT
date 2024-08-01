package com.kaitech.student_crm.models;

import javax.persistence.*;

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

    public Level() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPointFrom() {
        return pointFrom;
    }

    public void setPointFrom(Integer pointFrom) {
        this.pointFrom = pointFrom;
    }

    public Integer getPointTo() {
        return pointTo;
    }

    public void setPointTo(Integer pointTo) {
        this.pointTo = pointTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}