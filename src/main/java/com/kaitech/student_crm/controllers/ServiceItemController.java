package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.exceptions.ItemNotFound;
import com.kaitech.student_crm.payload.request.ServiceItemRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ServiceItemResponse;
import com.kaitech.student_crm.services.ServiceItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/service/item")
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
    @GetMapping("/{serviceItemId}")
    public ResponseEntity<ServiceItemResponse> findServiceItemById(@PathVariable Long serviceItemId) {
        return ResponseEntity.ok(serviceItemService.findByIdItem(serviceItemId));
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
    @PutMapping("update/item/by/{serviceItemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceItemResponse> updateServiceItem(@PathVariable Long serviceItemId,
                                                                 @Valid @RequestBody ServiceItemRequest request) {
        return ResponseEntity.ok(serviceItemService.updateItemById(serviceItemId, request));
    }

    @Operation(summary = "Удаление элемента сервиса по ID")
    @DeleteMapping("delete/item/by/{serviceItemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteServiceItem(@PathVariable Long serviceItemId) {
        return ResponseEntity.ok().body(serviceItemService.deleteItemById(serviceItemId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFound.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(ItemNotFound e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}