package com.kaitech.student_crm.models;

import com.kaitech.student_crm.models.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phoneNumber;
    private boolean registered = false;
    private Integer code;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;
    @ManyToMany(mappedBy = "students")
    private Set<Project> projects;
    private Integer point;

}