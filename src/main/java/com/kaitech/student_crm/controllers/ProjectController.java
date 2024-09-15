package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.request.ProjectRequest;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Вывод всех проектов")
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Вывод проекта по id")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @Operation(summary = "Создание нового проекта")
    public ProjectResponse createProject(
            @RequestBody ProjectRequest projectRequest,
            @RequestParam(defaultValue = "") List<Long> studIds) {

        return projectService.createProject(projectRequest, studIds);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Изменение проекта")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest projectRequest) {
        return projectService.updateProject(id, projectRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удаление проекта")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("add/student/{projectId}/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавление студента в проект")
    public ProjectResponse addStudentToProject(@PathVariable Long projectId,
                                               @PathVariable Long studentId) {
        return projectService.addStudentToProject(projectId, studentId);
    }

    @DeleteMapping("/{projectId}/students/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удаление студента из проекта")
    public ResponseEntity<HttpStatus> removeStudentFromProject(@PathVariable Long projectId,
                                                               @PathVariable Long studentId) {
        projectService.removeStudentFromProject(projectId, studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("add/students/{projectId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавляет много студентов")
    public ProjectResponse saveAllStudent(@PathVariable Long projectId,
                                          @RequestParam List<Long> studentIds) {
        return projectService.saveAllStudentInProject(projectId, studentIds);
    }

    @PostMapping("adds/students/{projectId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавляет одновременно множество студентов в проект")
    public ProjectResponse saveAllStudentPost(@PathVariable Long projectId,
                                              @RequestParam List<Long> studentIds) {
        return projectService.saveAllStudentInProject(projectId, studentIds);
    }

    @GetMapping("find/all/project/student")
    @Operation(summary = "Вывод проектов для студента",
            description = "Выводит все проекты в которых состоит студент. Для использование нужно войти аккаунт")
    public List<ProjectResponse> projectResponsesStudent() {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return projectService.findAllResponseByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("project/by/{projectId}/student")
    @Operation(summary = "Вывод проекта по id для студента",
            description = """
                    Выводит проект по id, а если у студента нету доступа к этому проекту то выводит ошибку.
                     Для использование нужно войти аккаунт
                    """)
    public ProjectResponse getProjectByIdStudent(@PathVariable Long projectId) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return projectService.getProjectByIdStudent(
                projectId,
                SecurityContextHolder.getContext().getAuthentication().getName());
    }
}