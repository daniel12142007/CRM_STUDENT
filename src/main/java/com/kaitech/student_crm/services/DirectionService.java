package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.request.DirectionCreateRequest;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import com.kaitech.student_crm.repositories.DirectionRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DirectionService {

    private final DirectionRepository directionRepository;
    private final StudentUserRepository studentUserRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(DirectionService.class);

    @Autowired
    public DirectionService(DirectionRepository directionRepository, StudentUserRepository studentUserRepository) {
        this.directionRepository = directionRepository;
        this.studentUserRepository = studentUserRepository;
    }

    public DirectionResponse createDirection(DirectionCreateRequest directionCreateRequest) {
        try {
            LOGGER.info("Создание нового Direction с именем: {}", directionCreateRequest.getName());

            if (directionCreateRequest.getName() == null || directionCreateRequest.getName().isEmpty()) {
                LOGGER.error("Поле имя не должно быть пустым");
                throw new IllegalArgumentException("Поле имя не должно быть пустым");
            }
            if (directionCreateRequest.getDescription() == null || directionCreateRequest.getDescription().isEmpty()) {
                LOGGER.error("Описание не должно быть пустым");
                throw new IllegalArgumentException("Описание не должно быть пустым");
            }

            if (directionRepository.existsByName(directionCreateRequest.getName())) {
                LOGGER.error("Направление с именем {} уже существует", directionCreateRequest.getName());
                throw new IllegalArgumentException("Направление с таким именем уже существует");
            }

            Direction direction = new Direction();
            direction.setName(directionCreateRequest.getName());
            direction.setDescription(directionCreateRequest.getDescription());

            Direction savedDirection = directionRepository.save(direction);
            LOGGER.info("Direction с id: {} успешно создан", savedDirection.getId());

            DirectionResponse responseDTO = new DirectionResponse();
            responseDTO.setId(savedDirection.getId()); // Устанавливаем id из сохраненного объекта
            responseDTO.setName(savedDirection.getName());
            responseDTO.setDescription(savedDirection.getDescription());

            return responseDTO;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка валидации данных: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (DataAccessException e) {
            LOGGER.error("Ошибка базы данных при создании Direction: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла ошибка базы данных", e);
        } catch (Exception e) {
            LOGGER.error("Неожиданная ошибка при создании Direction: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла неожиданная ошибка", e);
        }
    }

    public Direction assignStudentToDirection(Long directionId, Long studentId) {
        LOGGER.info("Назначение студента с id: {} к Direction с id: {}", studentId, directionId);

        Optional<Direction> optionalDirection = directionRepository.findById(directionId);
        Optional<Student> optionalStudent = studentUserRepository.findById(studentId);

        if (optionalDirection.isPresent() && optionalStudent.isPresent()) {
            Direction direction = optionalDirection.get();
            Student student = optionalStudent.get();

            student.setDirection(direction);
            studentUserRepository.save(student);

            direction.getStudents().add(student);
            LOGGER.info("Студент с id: {} успешно назначен к Direction с id: {}", studentId, directionId);
            return directionRepository.save(direction);
        } else {
            LOGGER.error("Direction с id: {} или Student с id: {} не найден", directionId, studentId);
            throw new IllegalArgumentException("Direction or Student not found");
        }
    }

    public void deleteDirection(Long id) {
        LOGGER.info("Удаление Direction с id: {}", id);
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Direction not found"));

        directionRepository.delete(direction);
        LOGGER.info("Direction с id: {} успешно удален", id);
    }

    public List<DirectionResponse> getAllDirections() {
        LOGGER.info("Получение всех Directions");
        return directionRepository.findAllDirections();
    }

    public DirectionResponse getById(Long id) {
        LOGGER.info("Получение Direction по id: {}", id);
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Direction not found with id: " + id));

        DirectionResponse response = new DirectionResponse();
        response.setId(direction.getId());
        response.setName(direction.getName());
        response.setDescription(direction.getDescription());

        LOGGER.info("Direction с id: {} успешно найден", id);
        return response;
    }

    public DirectionResponse updateDirection(Long id, DirectionCreateRequest directionCreateRequest) {
        try {
            LOGGER.info("Обновление Direction с ID: {}", id);

            if (directionCreateRequest.getName() == null || directionCreateRequest.getName().isEmpty()) {
                LOGGER.error("Поле имя не должно быть пустым");
                throw new IllegalArgumentException("Поле имя не должно быть пустым");
            }
            if (directionCreateRequest.getDescription() == null || directionCreateRequest.getDescription().isEmpty()) {
                LOGGER.error("Описание не должно быть пустым");
                throw new IllegalArgumentException("Описание не должно быть пустым");
            }

            Direction existingDirection = directionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Direction с ID: " + id + " не найден"));

            if (directionRepository.existsByNameAndIdNot(directionCreateRequest.getName(), id)) {
                LOGGER.error("Направление с именем {} уже существует", directionCreateRequest.getName());
                throw new IllegalArgumentException("Направление с таким именем уже существует");
            }

            existingDirection.setName(directionCreateRequest.getName());
            existingDirection.setDescription(directionCreateRequest.getDescription());

            Direction updatedDirection = directionRepository.save(existingDirection);
            LOGGER.info("Direction с ID: {} успешно обновлён", updatedDirection.getId());

            DirectionResponse responseDTO = new DirectionResponse();
            responseDTO.setId(updatedDirection.getId());
            responseDTO.setName(updatedDirection.getName());
            responseDTO.setDescription(updatedDirection.getDescription());

            return responseDTO;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка валидации данных: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error("Ошибка поиска Direction: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (DataAccessException e) {
            LOGGER.error("Ошибка базы данных при обновлении Direction: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла ошибка базы данных", e);
        } catch (Exception e) {
            LOGGER.error("Неожиданная ошибка при обновлении Direction: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла неожиданная ошибка", e);
        }
    }

}
