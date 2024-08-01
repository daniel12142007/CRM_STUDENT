package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.ActivityDTO;
import com.kaitech.student_crm.dtos.WeeksdayDTO;
import com.kaitech.student_crm.models.Activity;
import com.kaitech.student_crm.models.Weeksday;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.services.ActivityService;
import com.kaitech.student_crm.services.StudentUserService;
import com.kaitech.student_crm.services.WeeksdayService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DropdownController {

    private final StudentUserService studentUserService;
    private final ActivityService activityService;
    private final WeeksdayService weeksdayService;
    private final ModelMapper modelMapper;

    @Autowired
    public DropdownController(StudentUserService studentUserService, ActivityService activityService, WeeksdayService weeksdayService, ModelMapper modelMapper) {
        this.studentUserService = studentUserService;
        this.activityService = activityService;
        this.weeksdayService = weeksdayService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "Получение всех студентов")
    public List<StudentResponse> getAllStudents() {
        return studentUserService.getAllStudents();
    }

    @GetMapping("/activities")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "Получение всех активностей")
    public List<ActivityDTO> getAllActivities() {
        List<Activity> activities = activityService.findAll();
        return activities.stream()
                .map(this::convertToActivityDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/weeksdays")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @Operation(summary = "Получение всех дней недели")
    public List<WeeksdayDTO> getAllWeeksdays() {
        List<Weeksday> weeksdays = weeksdayService.getAllWeeksdays();
        return weeksdays.stream()
                .map(this::convertToWeeksdayDTO)
                .collect(Collectors.toList());
    }

    private ActivityDTO convertToActivityDTO(Activity activity) {
        return modelMapper.map(activity, ActivityDTO.class);
    }

    private WeeksdayDTO convertToWeeksdayDTO(Weeksday weeksday) {
        return modelMapper.map(weeksday, WeeksdayDTO.class);
    }
}
