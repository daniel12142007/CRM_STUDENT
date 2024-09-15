package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.request.ServiceItemRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ServiceItemResponse;
import com.kaitech.student_crm.services.ServiceItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/item")
public class ServiceItemController {
    private final ServiceItemService serviceItemService;

    public ServiceItemController(ServiceItemService serviceItemService) {
        this.serviceItemService = serviceItemService;
    }

    @Operation(summary = "Создание нового элемента сервиса")
    @PostMapping("create/item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceItemResponse> createServiceItem(@Valid @RequestBody ServiceItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceItemService.createItem(request));
    }

    @Operation(summary = "Получение элемента сервиса по ID")
    @GetMapping("/{itemId}")
    public ResponseEntity<ServiceItemResponse> findServiceItemById(@PathVariable Long itemId) {
        return ResponseEntity.ok(serviceItemService.findByIdItem(itemId));
    }

    @Operation(summary = "Получение всех элементов сервиса")
    @GetMapping("find/all/item")
    public ResponseEntity<List<ServiceItemResponse>> findAllServiceItems() {
        return ResponseEntity.ok(serviceItemService.findAllItem());
    }

    @Operation(summary = "Получение всех элементов сервиса которые связаны с Service")
    @GetMapping("find/all/item/by/{serviceId}")
    public ResponseEntity<List<ServiceItemResponse>> findAllItemByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(serviceItemService.findAllItemByServiceId(serviceId));
    }

    @Operation(summary = "Обновление элемента сервиса по ID")
    @PutMapping("update/item/by/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceItemResponse> updateServiceItem(@PathVariable Long itemId,
                                                                 @Valid @RequestBody ServiceItemRequest request) {
        return ResponseEntity.ok(serviceItemService.updateItemById(itemId, request));
    }

    @Operation(summary = "Удаление элемента сервиса по ID")
    @DeleteMapping("delete/item/by/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteServiceItem(@PathVariable Long itemId) {
        return ResponseEntity.ok().body(serviceItemService.deleteItemById(itemId));
    }
}