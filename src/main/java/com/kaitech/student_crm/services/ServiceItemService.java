package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.ServiceItem;
import com.kaitech.student_crm.payload.request.ServiceItemRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ServiceItemResponse;
import com.kaitech.student_crm.repositories.ServiceItemRepository;
import com.kaitech.student_crm.repositories.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceItemService {
    private final Logger LOGGER = LoggerFactory.getLogger(ServiceItemService.class);
    private final ServiceItemRepository serviceItemRepository;
    private final ServicesRepository servicesRepository;

    @Autowired
    public ServiceItemService(ServiceItemRepository serviceItemRepository, ServicesRepository servicesRepository) {
        this.serviceItemRepository = serviceItemRepository;
        this.servicesRepository = servicesRepository;
    }

    public ServiceItemResponse createItem(ServiceItemRequest request) {

        if (servicesRepository.findById(request.serviceId()).isEmpty()) {
            LOGGER.error("Service с ID {} не найден", request.serviceId());
            throw new NotFoundException("Not found Service ID: " + request.serviceId());
        }

        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setTitle(request.title());
        serviceItem.setDescription(request.description());
        serviceItem.setServices(servicesRepository.findById(request.serviceId()).get());
        serviceItemRepository.save(serviceItem);

        LOGGER.info("Создан новый service item с ID {}", serviceItem.getId());
        return serviceItemRepository.findByIdResponse(serviceItem.getId())
                .orElseThrow(() -> {
                    LOGGER.error("Service item с ID {} не найден", serviceItem.getId());
                    return new NotFoundException("Not found Service Item ID: " + serviceItem.getId());
                });
    }

    public ServiceItemResponse findByIdItem(Long id) {
        return serviceItemRepository.findByIdResponse(id)
                .orElseThrow(() -> {
                    LOGGER.error("Service item с ID {} не найден", id);
                    return new NotFoundException("Not found Service item ID: " + id);
                });
    }

    public List<ServiceItemResponse> findAllItem() {
        return serviceItemRepository.findAllResponse();
    }

    public List<ServiceItemResponse> findAllItemByServiceId(Long serviceId) {
        return serviceItemRepository.findAllByServiceId(serviceId);
    }

    @Transactional
    public ServiceItemResponse updateItemById(Long id, ServiceItemRequest request) {
        Optional<ServiceItem> optionalServiceItem = serviceItemRepository.findById(id);
        if (optionalServiceItem.isEmpty()) {
            LOGGER.error("Service item с ID {} не найден", id);
            throw new NotFoundException("Not found Service ID: " + id);
        }

        ServiceItem serviceItem = optionalServiceItem.get();
        if (servicesRepository.findById(request.serviceId()).isEmpty()) {
            LOGGER.error("Service с ID {} не найден", request.serviceId());
            throw new NotFoundException("Not found Service ID: " + request.serviceId());
        }

        serviceItem.setTitle(request.title());
        serviceItem.setDescription(request.description());
        serviceItem.setServices(servicesRepository.findById(request.serviceId()).get());

        serviceItem = serviceItemRepository.save(serviceItem);
        LOGGER.info("Обновлен service item с ID {}", serviceItem.getId());
        return serviceItemRepository.findByIdResponse(id)
                .orElseThrow(() -> {
                    LOGGER.error("Service item с ID {} не найден", id);
                    return new NotFoundException("Not found Service item ID: " + id);
                });
    }

    public MessageResponse deleteItemById(Long id) {
        if (!serviceItemRepository.existsById(id)) {
            LOGGER.error("Service item с ID {} не найден", id);
            throw new NotFoundException("Not found Service item ID: " + id);
        }
        serviceItemRepository.deleteById(id);
        LOGGER.info("Удален service item с ID {}", id);
        return new MessageResponse("Successfully removed service item");
    }
}
