package com.kaitech.student_crm.dtos;

public class ReportResponseDTO {
    private Long id;
    private StudentDTO student;
    private ActivityDTO activity;
    private WeeksdayDTO weeksday;
    private boolean isDone;


    public ReportResponseDTO(Long id, StudentDTO student, ActivityDTO activity, WeeksdayDTO weeksday, boolean isDone) {
        this.id = id;
        this.student = student;
        this.activity = activity;
        this.weeksday = weeksday;
        this.isDone = isDone;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public WeeksdayDTO getWeeksday() {
        return weeksday;
    }

    public void setWeeksday(WeeksdayDTO weeksday) {
        this.weeksday = weeksday;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
