package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.ProjectAlreadyCompletedException;
import com.kaitech.student_crm.exceptions.ProjectNotFoundException;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.request.ProjectRequest;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.repositories.ProjectRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final StudentUserRepository studentUserRepository;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    public ProjectService(ProjectRepository projectRepository, StudentUserRepository studentUserRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.studentUserRepository = studentUserRepository;
        this.modelMapper = modelMapper;
    }

    public ProjectResponse saveAllStudentInProject(Long projectId, List<Long> studentIds) {
        LOGGER.info("Добавление студентов в проект с id: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + projectId + " не найден"));

        LocalDate today = LocalDate.now();
        if (project.getEndDate() != null && !project.getEndDate().isAfter(today)) {
            LOGGER.error("Нельзя добавлять студентов в завершенный проект с id: {}", projectId);
            throw new ProjectAlreadyCompletedException("Нельзя добавлять студентов в завершенный проект");
        }

        List<Student> students = studentUserRepository.findAllById(studentIds);
        project.getStudents().addAll(students);
        projectRepository.save(project);

        LOGGER.info("Студенты успешно добавлены в проект с id: {}", projectId);
        return findByIdResponse(project.getId());
    }

    public List<ProjectResponse> getAllProjects() {
        LOGGER.info("Получение всех проектов");
        return projectRepository.findAllResponse();
    }

    public ProjectResponse getProjectById(Long id) {
        LOGGER.info("Получение проекта с id: {}", id);
        ProjectResponse projectResponse = projectRepository.findByIdResponse(id);

        if (projectResponse == null) {
            LOGGER.error("Проект с id: {} не найден", id);
            throw new ProjectNotFoundException("Project with id " + id + " not found");
        }

        List<StudentResponse> students = studentUserRepository.findAllByProjectIdResponse(id);
        return new ProjectResponse(
                projectResponse.id(),
                projectResponse.title(),
                projectResponse.description(),
                projectResponse.projectType(),
                students,
                projectResponse.startDate(),
                projectResponse.endDate()
        );
    }

    public ProjectResponse findByIdResponse(Long projectId) {
        LOGGER.info("Получение проекта с id: {} и его студентов", projectId);
        ProjectResponse response = projectRepository.findByIdResponse(projectId);
        List<StudentResponse> students = studentUserRepository.findAllByProjectId(projectId);
        return new ProjectResponse(
                response.id(),
                response.title(),
                response.description(),
                response.projectType(),
                students,
                response.startDate(),
                response.endDate()
        );
    }

    public ProjectResponse createProject(ProjectRequest projectRequest, List<Long> studentIds) {
        try {
            LOGGER.info("Создание нового проекта с заголовком: {}", projectRequest.getTitle());
            if (projectRepository.existsByTitle(projectRequest.getTitle())) {
                LOGGER.error("Проект с заголовком {} уже существует", projectRequest.getTitle());
                throw new IllegalArgumentException("Проект с таким заголовком уже существует");
            }

            LocalDate today = LocalDate.now();
            if (projectRequest.getStartDate() == null || projectRequest.getStartDate().isBefore(today)) {
                LOGGER.error("Дата начала должна быть сегодня или в будущем");
                throw new IllegalArgumentException("Дата начала должна быть сегодня или в будущем");
            }

            if (projectRequest.getEndDate() != null && !projectRequest.getEndDate().isAfter(projectRequest.getStartDate())) {
                LOGGER.error("Дата окончания должна быть не меньше и не равна дате начала");
                throw new IllegalArgumentException("Дата окончания должна быть не меньше и не равна дате начала");
            }

            Project newProject = new Project();
            newProject.setTitle(projectRequest.getTitle());
            newProject.setDescription(projectRequest.getDescription());
            newProject.setProjectType(projectRequest.getProjectType());
            newProject.setStartDate(projectRequest.getStartDate());
            newProject.setEndDate(projectRequest.getEndDate());
            newProject.getStudents().addAll(studentUserRepository.findAllById(studentIds));

            projectRepository.save(newProject);
            LOGGER.info("Проект с id: {} успешно создан", newProject.getId());
            return findByIdResponse(newProject.getId());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка валидации данных: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Произошла непредвиденная ошибка: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла непредвиденная ошибка", e);
        }
    }

    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
        LOGGER.info("Обновление проекта с id: {}", id);
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + id + " не найден"));

        LocalDate oldStartDate = existingProject.getStartDate();
        LocalDate oldEndDate = existingProject.getEndDate();
        LocalDate newStartDate = projectRequest.getStartDate();
        LocalDate newEndDate = projectRequest.getEndDate();

        if (newStartDate != null && newStartDate.isBefore(oldStartDate)) {
            LOGGER.error("Новая дата начала не может быть раньше предыдущей даты начала");
            throw new IllegalArgumentException("Новая дата начала не может быть раньше предыдущей даты начала");
        }

        if (newEndDate != null && newEndDate.isBefore(oldEndDate)) {
            LOGGER.error("Новая дата окончания не может быть раньше предыдущей даты окончания");
            throw new IllegalArgumentException("Новая дата окончания не может быть раньше предыдущей даты окончания");
        }

        if (newStartDate != null && newEndDate != null && !newEndDate.isAfter(newStartDate)) {
            LOGGER.error("Новая дата окончания должна быть не меньше и не равна новой дате начала");
            throw new IllegalArgumentException("Новая дата окончания должна быть не меньше и не равна новой дате начала");
        }

        if (projectRequest.getTitle() != null && !projectRequest.getTitle().equals(existingProject.getTitle())) {
            boolean titleExists = projectRepository.existsByTitle(projectRequest.getTitle());
            if (titleExists) {
                LOGGER.error("Проект с названием {} уже существует", projectRequest.getTitle());
                throw new IllegalArgumentException("Проект с таким названием уже существует");
            }
        }

        existingProject.setTitle(projectRequest.getTitle());
        existingProject.setDescription(projectRequest.getDescription());
        existingProject.setProjectType(projectRequest.getProjectType());
        existingProject.setStartDate(newStartDate != null ? newStartDate : oldStartDate);
        existingProject.setEndDate(newEndDate != null ? newEndDate : oldEndDate);

        projectRepository.save(existingProject);
        LOGGER.info("Проект с id: {} успешно обновлен", id);
        return findByIdResponse(existingProject.getId());
    }

    public void deleteProject(Long id) {
        LOGGER.info("Удаление проекта с id: {}", id);
        ProjectResponse projectResponse = getProjectById(id);

        if (projectResponse == null) {
            LOGGER.error("Проект с id: {} не найден", id);
            throw new ProjectNotFoundException("Project with id " + id + " not found");
        }
        projectRepository.delete(convertToProject(projectResponse));
        LOGGER.info("Проект с id: {} успешно удален", id);
    }

    public ProjectResponse addStudentToProject(Long projectId, Long studentId) {
        LOGGER.info("Добавление студента с id: {} в проект с id: {}", studentId, projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Проект с id " + projectId + " не найден"));

        LocalDate today = LocalDate.now();
        if (project.getEndDate() != null && !project.getEndDate().isAfter(today)) {
            LOGGER.error("Нельзя добавлять студента в завершенный проект с id: {}", projectId);
            throw new ProjectAlreadyCompletedException("Нельзя добавлять студента в завершенный проект");
        }

        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));

        project.getStudents().add(student);
        projectRepository.save(project);

        ProjectResponse projectResponse = projectRepository.findByIdResponse(projectId);
        List<StudentResponse> students = studentUserRepository.findAllByProjectIdResponse(projectId);
        return new ProjectResponse(
                projectResponse.id(),
                projectResponse.title(),
                projectResponse.description(),
                projectResponse.projectType(),
                students,
                projectResponse.startDate(),
                projectResponse.endDate()
        );
    }

    public void removeStudentFromProject(Long projectId, Long studentId) {
        LOGGER.info("Удаление студента с id: {} из проекта с id: {}", studentId, projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id " + projectId + " not found"));
        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        project.getStudents().remove(student);
        projectRepository.save(project);
        LOGGER.info("Студент с id: {} успешно удален из проекта с id: {}", studentId, projectId);
    }

    public Project convertToProject(ProjectResponse projectResponse) {
        return modelMapper.map(projectResponse, Project.class);
    }
}