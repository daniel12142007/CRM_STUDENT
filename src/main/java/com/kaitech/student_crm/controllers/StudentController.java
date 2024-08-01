package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.StudentDTOForAll;
import com.kaitech.student_crm.exceptions.EmailAlreadyExistsException;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.enums.Status;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.services.StudentUserService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/student")
@Validated
public class StudentController {

    private final StudentUserService studentUserService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public StudentController(StudentUserService studentUserService, ModelMapper modelMapper, ResponseErrorValidation responseErrorValidation) {
        this.studentUserService = studentUserService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

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

    @PostMapping("/add/intern/{directionId}")
    @Operation(summary = "Добавление нового стажера", description = "Этот метод может использовать только ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StudentResponse addStudent(@Valid @RequestBody StudentDataRequest studentDataRequest,
                                      @RequestParam Status status,
                                      @PathVariable Long directionId) {
        return studentUserService.createStudent(studentDataRequest, status, directionId);
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Удаление студента по идентификатору")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable("id") String studentId) {
        studentUserService.deleteStudent(Long.parseLong(studentId));
        return new ResponseEntity<>(new MessageResponse("Student was deleted"), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "Обновление данных студента")
    public ResponseEntity<Object> updateStudent(
            @PathVariable("id") String studentId,
            @Valid @RequestBody StudentDTO studentDTO,
            BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        try {
            Student student = studentUserService.updateStudent(Long.parseLong(studentId), studentDTO);
            StudentDTO updatedStudent = convertToStudentDTO(student);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
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

    private StudentDTO convertToStudentDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<MessageResponse> handleLevelNotFound(StudentNotFoundException e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}