package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.WeeksdayDTO;
import com.kaitech.student_crm.models.Weeksday;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.WeeksdayService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/weeksday")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class WeeksdayController {

    private final WeeksdayService weeksdayService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public WeeksdayController(WeeksdayService weeksdayService, ResponseErrorValidation responseErrorValidation) {
        this.weeksdayService = weeksdayService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение дня недели по идентификатору")
    public ResponseEntity<Weeksday> getWeeksday(@PathVariable("id") String weeksdayId) {
        Weeksday weeksday = weeksdayService.getWeeksdayById(Long.parseLong(weeksdayId));
        return new ResponseEntity<>(weeksday, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение списка всех дней недели")
    public ResponseEntity<List<Weeksday>> getAllWeeksday() {
        List<Weeksday> weeksdays = weeksdayService.getAllWeeksdays();
        return new ResponseEntity<>(weeksdays, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание нового дня недели")
    public ResponseEntity<Object> createWeeksday(@Valid @RequestBody WeeksdayDTO weeksdayDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Weeksday newWeeksday = weeksdayService.createWeeksday(weeksdayDTO);
        return new ResponseEntity<>(newWeeksday, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Удаление дня недели по идентификатору")
    public ResponseEntity<Object> deleteWeeksday(@PathVariable("id") String weeksdayId) {
        weeksdayService.deleteWeeksday(Long.parseLong(weeksdayId));
        return new ResponseEntity<>(new MessageResponse("Week's day was deleted"), HttpStatus.OK);
    }
}