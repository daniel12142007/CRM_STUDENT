package com.kaitech.student_crm.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Data
@NoArgsConstructor
@Entity
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 900)
    private String description;
    private Integer price;
    @OneToMany(mappedBy = "services", cascade = CascadeType.REMOVE)
    private List<ServiceItem> serviceItems;


    public Services(String title, String description, Integer price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public Services(Long id, String title, String description, Integer price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
    }

}