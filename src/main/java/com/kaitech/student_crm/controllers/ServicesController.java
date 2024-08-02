package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.request.ServicesRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ServicesResponse;
import com.kaitech.student_crm.services.ServicesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/services")
public class ServicesController {
    private final ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @Operation(summary = "Создание новой услуги")
    @PostMapping("create/service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServicesResponse> createService(@Valid @RequestBody ServicesRequest request) {
        return ResponseEntity.ok(servicesService.createService(request));
    }

    @Operation(summary = "Обновление услуги по ID")
    @PutMapping("/{serviceId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServicesResponse> updateByServiceId(@PathVariable Long serviceId,
                                                              @Valid @RequestBody ServicesRequest request) {
        return ResponseEntity.ok(servicesService.updateByServiceId(serviceId, request));
    }

    @Operation(summary = "Поиск услуги по ID")
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServicesResponse> findById(@PathVariable Long serviceId) {
        return ResponseEntity.ok(servicesService.findById(serviceId));
    }

    @Operation(summary = "Получение списка всех услуг")
    @GetMapping("find/all")
    public ResponseEntity<List<ServicesResponse>> findAll() {
        List<ServicesResponse> responseList = servicesService.findAll();
        if (responseList == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "Удаление услуги по ID")
    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(servicesService.deleteByServiceId(serviceId));
    }
}
