package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Services;
import com.kaitech.student_crm.payload.request.ServicesRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ServicesResponse;
import com.kaitech.student_crm.repositories.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServicesService {
    private final Logger LOGGER = LoggerFactory.getLogger(ServicesService.class);
    private final ServicesRepository servicesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public ServicesResponse createService(ServicesRequest request) {
        LOGGER.info("Создание новой услуги: {}", request);
        Services services = new Services(request.title(), request.description(), request.price());
        servicesRepository.save(services);
        LOGGER.info("Услуга успешно создана с ID: {}", services.getId());
        return findById(services.getId());
    }

    public ServicesResponse updateByServiceId(Long serviceId, ServicesRequest request) {
        LOGGER.info("Обновление услуги с ID: {}", serviceId);
        Services services = servicesRepository.findById(serviceId).orElseThrow(
                () -> {
                    LOGGER.error("Услуга с ID: {} не найдена", serviceId);
                    return new NotFoundException("Not found service ID: " + serviceId);
                }
        );
        services = new Services(services.getId(), request.title(), request.description(), request.price());
        servicesRepository.save(services);
        LOGGER.info("Услуга с ID: {} успешно обновлена", serviceId);
        return findById(services.getId());
    }

    public ServicesResponse findById(Long serviceId) {
        LOGGER.info("Поиск услуги с ID: {}", serviceId);
        return servicesRepository.findByIdResponse(serviceId).orElseThrow(
                () -> {
                    LOGGER.error("Услуга с ID: {} не найдена", serviceId);
                    return new NotFoundException("Not found service ID: " + serviceId);
                }
        );
    }

    public List<ServicesResponse> findAll() {
        LOGGER.info("Получение списка всех услуг");
        return servicesRepository.findAllResponse();
    }

    public MessageResponse deleteByServiceId(Long serviceId) {
        LOGGER.info("Удаление услуги с ID: {}", serviceId);
        Services services = servicesRepository.findById(serviceId).orElseThrow(
                () -> {
                    LOGGER.error("Услуга с ID: {} не найдена", serviceId);
                    return new NotFoundException("Not found service ID: " + serviceId);
                }
        );
        servicesRepository.delete(services);
        LOGGER.info("Услуга с ID: {} успешно удалена", serviceId);
        return new MessageResponse("Successfully removed service");
    }
}