package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.LevelBadRequest;
import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Level;
import com.kaitech.student_crm.payload.request.LevelRequest;
import com.kaitech.student_crm.payload.response.LevelResponse;
import com.kaitech.student_crm.repositories.LevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LevelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LevelService.class);
    private final LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public LevelResponse createLevel(LevelRequest request) {
        Level level = new Level(request.title(), request.pointFrom(), request.pointTo(), request.description());
        checkPoint(request);
        if (levelRepository.existsByTitle(request.title())) {
            LOGGER.error("title: {} уже существует, название уровня должно быть уникальным", request.title());
            throw new LevelBadRequest("The level name must be unique");
        }
        if (!levelRepository.checkPoints(request.pointFrom()).isEmpty()) {
            var levels = levelRepository.checkPoints(request.pointFrom());
            LOGGER.error("pointFrom: {} оказался между уровнями баллов: {}", request.pointFrom(), levels);
            throw new LevelBadRequest("Point from: " + request.pointFrom() + " is between these levels:" + levels);
        }
        if (!levelRepository.checkPoints(request.pointTo()).isEmpty()) {
            var levels = levelRepository.checkPoints(request.pointTo());
            LOGGER.error("pointTo: {} оказался между уровнями баллов: {}", request.pointFrom(), levels);
            throw new LevelBadRequest("Point to: " + request.pointTo() + " is between these levels:" + levels);
        }
        levelRepository.save(level);
        LOGGER.info("Уровень с ID: {} успешно сохранён", level.getId());
        return findById(level.getId());
    }

    public LevelResponse updateLevel(LevelRequest request, Long levelId) {
        Level level = levelRepository.findById(levelId).orElseThrow(
                () -> new NotFoundException("Not found level ID:" + levelId)
        );
        checkPoint(request);
        if (levelRepository.existsByTitleAndIdNot(request.title(), levelId)) {
            LOGGER.error("title: {} уже существует, название уровня должно быть уникальным", request.title());
            throw new LevelBadRequest("The level name must be unique");
        }
        if (!levelRepository.checkPoints(request.pointFrom(), levelId).isEmpty()) {
            var levels = levelRepository.checkPoints(request.pointFrom());
            LOGGER.error("pointFrom: {} оказался между уровнями баллов: {}", request.pointFrom(), levels);
            throw new LevelBadRequest("Point from: " + request.pointFrom() + " is between these levels:" + levels);
        }
        if (!levelRepository.checkPoints(request.pointTo(), levelId).isEmpty()) {
            var levels = levelRepository.checkPoints(request.pointTo());
            LOGGER.error("pointTo: {} оказался между уровнями баллов: {}", request.pointFrom(), levels);
            throw new LevelBadRequest("Point to: " + request.pointTo() + " is between these levels:" + levels);
        }
        level.setTitle(request.title());
        level.setDescription(request.description());
        level.setPointFrom(request.pointFrom());
        level.setPointTo(request.pointTo());
        levelRepository.save(level);
        LOGGER.info("Уровень с ID: {} успешно обновлён", level.getId());
        return findById(level.getId());
    }

    public LevelResponse findById(Long levelId) {
        return levelRepository.findByIdResponse(levelId).orElseThrow(() -> {
            LOGGER.error("Не удалось найти уровень с ID: {}", levelId);
            return new NotFoundException("Level not found with ID: " + levelId);
        });
    }

    public LevelResponse findByTitle(String title) {
        return levelRepository.findByTitleResponse(title).orElseThrow(() -> {
            LOGGER.error("Не удалось найти уровень с Title: {}", title);
            return new NotFoundException("Level not found with Title: " + title);
        });
    }

    public List<LevelResponse> findAll() {
        return levelRepository.findAllResponse();
    }

    public List<LevelResponse> deleteById(Long levelId) {
        Level level = levelRepository.findById(levelId).orElseThrow(
                () -> new NotFoundException("Not found level ID:" + levelId)
        );
        try {
            levelRepository.delete(level);
            LOGGER.info("Уровень с ID: {} успешно удалён", levelId);
            return findAll();
        } catch (Exception e) {
            LOGGER.error("Не удалось удалить уровень с ID: {}", levelId);
            throw new RuntimeException("Failed delete level ID:" + level);
        }
    }

    private void checkPoint(LevelRequest request) {
        if (request.pointFrom() >= request.pointTo()) {
            LOGGER.error("pointFrom: {} не может быть больше или равен pointTo: {}", request.pointFrom(), request.pointTo());
            throw new LevelBadRequest("pointTo should be greater than pointFrom");
        }
    }
}