package com.kaitech.student_crm.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kaitech.student_crm.config.JwtUtils;
import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.StudentDTOForAll;
import com.kaitech.student_crm.exceptions.EmailAlreadyExistsException;
import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.enums.Status;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.request.StudentRegisterRequest;
import com.kaitech.student_crm.payload.request.StudentRequest;
import com.kaitech.student_crm.payload.request.UpdateStudentRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.services.StudentUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/student")
@Validated
@AllArgsConstructor
public class StudentController {

    private final StudentUserService studentUserService;
    private final JwtUtils jwtUtils;

    @GetMapping("/{studentId}")
    @Operation(summary = "Получение студента по идентификатору")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long studentId) {
        try {
            return new ResponseEntity<>(studentUserService.findByIdStudentInfo(studentId), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Получение списка всех студентов")
    public ResponseEntity<List<StudentDTOForAll>> getAllStudents() {
        List<StudentDTOForAll> students = studentUserService.findAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping(value = "/add/intern/{directionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление нового стажера", description = "Этот метод может использовать только ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StudentDTO addStudent(@RequestParam(name = "image", required = false) MultipartFile image,
                                 @RequestParam
                                 @Email(message = "It should have email format")
                                 @NotBlank(message = "Student email is required") String email,
                                 @RequestParam @NotEmpty(message = "Please enter student's name") String firstname,
                                 @RequestParam @NotEmpty(message = "Please enter student's lastname") String lastname,
                                 @RequestParam @NotEmpty(message = "Please enter student's phone number") String phoneNumber,
                                 @RequestParam Status status,
                                 @PathVariable Long directionId) {
        StudentDataRequest studentDataRequest = new StudentDataRequest();
        studentDataRequest.setEmail(email);
        studentDataRequest.setFirstname(firstname);
        studentDataRequest.setLastname(lastname);
        studentDataRequest.setPhoneNumber(phoneNumber);
        return studentUserService.createStudent(studentDataRequest, status, directionId, image);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удаление студента по идентификатору")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable("id") String studentId) {
        studentUserService.deleteStudent(Long.parseLong(studentId));
        return new ResponseEntity<>(new MessageResponse("Student was deleted"), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "Обновление данных студента", description = "может использовать только ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateStudent(
            @PathVariable("id") String studentId,
            @Valid @RequestBody StudentRequest request) {
        try {
            return new ResponseEntity<>(studentUserService.updateStudent(Long.parseLong(studentId), request), HttpStatus.OK);
        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/students/{id}/status")
    @Operation(summary = "Изменение статуса студента", description = "Этот метод может использовать только ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StudentResponse updateStudentStatus(@PathVariable Long id, @RequestParam Status status) {
        return studentUserService.updateStudentStatus(id, status);
    }

    @PutMapping("add/point/student/by/{studentId}")
    @Operation(summary = "Добавление баллов студенту", description = "Этот метод может использовать только ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentDTO> addPointStudent(@PathVariable Long studentId,
                                                      @RequestParam Integer point) {
        return ResponseEntity.ok(studentUserService.addPointForStudent(studentId, point));
    }

    @PutMapping("delete/student/image/{studentId}")
    @Operation(summary = "Метод для удаления изображения студента",
            description = "Доступен для ROLE_ADMIN или владельца аккаунта студента")
    public StudentDTO deleteImage(@PathVariable Long studentId) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return studentUserService.deleteImage(SecurityContextHolder.getContext().getAuthentication().getName(), studentId);
    }

    @PostMapping("registered/for/student/")
    @Operation(summary = "The student registers using this link")
    public StudentDTO registerStudent(
            @RequestBody StudentRegisterRequest request) {
        return studentUserService.registerStudent(request);
    }

    @PutMapping("/{studentId}/assign-level/{levelId}")
    @Operation(summary = "Метод для присвоения уровня студенту",
            description = "Доступен для ROLE_ADMIN ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StudentResponse assignLevelToStudent(@PathVariable Long studentId,
                                                @PathVariable Long levelId) {
        return studentUserService.updateLevel(studentId, levelId);
    }

    @GetMapping("/students/level/{levelId}")
    @Operation(summary = "Фильтррация студента по уровню")
    public List<StudentResponse> getStudentsByLevel(@PathVariable Long levelId) {
        return studentUserService.filterStudentsByLevel(levelId);
    }

    @PutMapping(value = "update/image/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Метод для обновления изображение студента")
    public StudentDTO updateImage(@PathVariable Long studentId,
                                  @RequestParam MultipartFile image) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            throw new AccessDeniedException("Login to your account");
        return studentUserService.updateImage(
                studentId,
                SecurityContextHolder.getContext().getAuthentication().getName(),
                image);
    }

    @GetMapping("/students/direction/{directionName}")
    @Operation(summary = "Фильтррация студента по направлению")
    public List<StudentResponse> getStudentByDirection(@PathVariable String directionName){
       return studentUserService.filterByDirection(directionName);
    }

    @GetMapping("/students/project/{projectId}")
    @Operation(summary = "Фильтррация студента по проекту")
    public List<StudentResponse> getStudentsByProjectId(@PathVariable Long projectId) {
        return studentUserService.filterByProject(projectId);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск студента по имени или email")
    public ResponseEntity<?> findStudentByNameOrEmail(@RequestParam String nameOrEmail) {
        if (nameOrEmail.contains("@")) {
            try {
                StudentResponse student = studentUserService.findStudentByEmail(nameOrEmail);
                return ResponseEntity.ok(student);
            } catch (NotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            List<StudentResponse> students = studentUserService.findStudentsByName(nameOrEmail);
            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(students);
        }
    }

    @PostMapping("/sendCode")
    @Operation(summary = "Отправка кода подтверждения для изменения на новый email")
    public MessageResponse sendVerificationCode(@RequestParam String newEmail) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return studentUserService.sendVerificationCode(email, newEmail);
    }

    @PostMapping("/changeEmail")
    @Operation(summary = "Подтверждение кода и обновление email")
    public String changeEmail(@RequestParam int code, @RequestParam String newEmail) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return studentUserService.verifyCodeAndChangeEmail(email, code, newEmail);
    }

    @GetMapping("/profile")
    @Operation(summary = "Просмотр профиля  студента ")
    public ResponseEntity<StudentResponse> getStudentProfile() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<StudentResponse> studentResponse = studentUserService.getStudentProfileByEmail(email);

        return studentResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }


    @PutMapping("/update-profile")
    @Operation(summary = "Редактирование профиля")
    public ResponseEntity<String> updateProfileByToken(@RequestBody UpdateStudentRequest studentRequest) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        studentUserService.updateStudentDetails(email, studentRequest.firstName(), studentRequest.lastName(), studentRequest.phoneNumber());

        return ResponseEntity.ok("Profile updated successfully");
    }

}