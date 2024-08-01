package com.kaitech.student_crm.models;

import javax.persistence.*;

@Entity
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "services_id")
    private Services services;

    public ServiceItem() {
    }

    public ServiceItem(Long id, String title, String description, Services services) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.services = services;
    }

    public ServiceItem(String title, String description, Services services) {
        this.title = title;
        this.description = description;
        this.services = services;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }
}