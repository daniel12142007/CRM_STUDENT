package com.kaitech.student_crm.dtos;


import javax.validation.constraints.NotEmpty;

public class WeeksdayDTO {

    private Long id;
    @NotEmpty
    private String name;

    public WeeksdayDTO() {
    }

    public WeeksdayDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty String getName() {
        return name;
    }

    public void setName(@NotEmpty String name) {
        this.name = name;
    }
}
