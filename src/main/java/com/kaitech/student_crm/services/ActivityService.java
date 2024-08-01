package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.ActivityNotFoundException;
import com.kaitech.student_crm.models.Activity;
import com.kaitech.student_crm.repositories.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


    public List<Activity> findAll() {
        LOGGER.info("Получение списка всех Activities");
        return activityRepository.findAll();
    }

    public Optional<Activity> findById(Long id) {
        LOGGER.info("Поиск Activity по ID: {}", id);
        return activityRepository.findById(id);
    }

    @Transactional
    public Activity addActivity(Activity activity) {
        LOGGER.info("Добавление новой Activity: {}", activity);
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updatedActivity) {
        LOGGER.info("Обновление Activity с ID: {}", id);
        Optional<Activity> existingActivityOpt = activityRepository.findById(id);
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = existingActivityOpt.get();
            existingActivity.setTitle(updatedActivity.getTitle());
            existingActivity.setDescription(updatedActivity.getDescription());
            LOGGER.info("Activity с ID: {} обновлена", id);
            return activityRepository.save(existingActivity);
        } else {
            LOGGER.error("Activity с ID: {} не найдена", id);
            throw new ActivityNotFoundException("Activity not found with id " + id);
        }
    }

    public void deleteActivity(Long id) {
        LOGGER.info("Удаление Activity с ID: {}", id);
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
            LOGGER.info("Activity с ID: {} успешно удалена", id);
        } else {
            LOGGER.error("Activity с ID: {} не найдена", id);
            throw new ActivityNotFoundException("Activity not found with id " + id);
        }
    }
}