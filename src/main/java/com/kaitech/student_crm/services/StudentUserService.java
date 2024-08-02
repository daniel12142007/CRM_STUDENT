package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.StudentDTOForAll;
import com.kaitech.student_crm.exceptions.EmailAlreadyExistsException;
import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.models.enums.Status;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.repositories.DirectionRepository;
import com.kaitech.student_crm.repositories.ProjectRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import com.kaitech.student_crm.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentUserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(StudentUserService.class);
    private final StudentUserRepository studentUserRepository;
    private final DirectionRepository directionRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;
    @Value("${link}")
    private String link;

    @Autowired
    public StudentUserService(StudentUserRepository studentUserRepository, DirectionRepository directionRepository, JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder, ProjectRepository projectRepository) {
        this.studentUserRepository = studentUserRepository;
        this.directionRepository = directionRepository;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
    }

    public StudentResponse createStudent(StudentDataRequest student,
                                         Status status,
                                         Long directionId) {
        LOGGER.info("Попытка создания нового студента с email: {}", student.getEmail());

        if (directionRepository.findById(directionId).isEmpty()) {
            LOGGER.error("Направление с ID {} не найдено", directionId);
            throw new RuntimeException("There is no such direction.");
        }
        if (studentUserRepository.existsByEmail(student.getEmail()) || userRepository.existsByEmail(student.getEmail())) {
            LOGGER.error("Email {} уже существует", student.getEmail());
            throw new RuntimeException("This email already exists. Email must be unique.");
        }

        Random random = new Random();
        Integer randomCode = random.nextInt(100000000, 999999999);
        Student newStudent = new Student();
        newStudent.setLastName(student.getFirstname());
        newStudent.setFirstName(student.getLastname());
        newStudent.setCode(randomCode);
        newStudent.setImage(student.getImage());
        newStudent.setStatus(status);
        newStudent.setEmail(student.getEmail());
        newStudent.setDirection(directionRepository.findById(directionId).get());
        newStudent.setPhoneNumber(student.getPhoneNumber());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(newStudent.getEmail());
            message.setSubject("Ссылка для регистрации работает только 1 раз");
            message.setText("Ваша ссылка для регистрации:" + link + "/" + newStudent.getEmail() + "/" + randomCode);
            javaMailSender.send(message);
        } catch (MailException e) {
            LOGGER.error("Ошибка при отправке письма на email: {}", newStudent.getEmail());
            throw new RuntimeException("Please enter a valid email address.");
        }

        try {
            LOGGER.info("Сохранение студента {}", student.getEmail());
            studentUserRepository.save(newStudent);
        } catch (Exception e) {
            LOGGER.error("Ошибка при регистрации, {}", e.getMessage());
            throw new UserExistException("The student " + newStudent.getFirstName() + " " + newStudent.getLastName() + " already exists");
        }

        return findById(newStudent.getId());
    }

    public Student updateStudent(Long studentId, StudentDTO updatedStudentDTO) {
        LOGGER.info("Обновление студента с ID: {}", studentId);

        Optional<Student> studentOptional = studentUserRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            if (!student.getEmail().equals(updatedStudentDTO.getEmail())) {
                boolean emailExists = studentUserRepository.existsByEmail(updatedStudentDTO.getEmail());
                if (emailExists) {
                    LOGGER.error("Email {} уже используется", updatedStudentDTO.getEmail());
                    throw new EmailAlreadyExistsException("Email " + updatedStudentDTO.getEmail() + " is already in use");
                }
            }

            student.setFirstName(updatedStudentDTO.getFirstname());
            student.setLastName(updatedStudentDTO.getLastname());
            student.setEmail(updatedStudentDTO.getEmail());
            student.setPhoneNumber(updatedStudentDTO.getPhoneNumber());

            Student updatedStudent = studentUserRepository.save(student);
            LOGGER.info("Студент с ID: {} успешно обновлён", studentId);
            return updatedStudent;
        } else {
            LOGGER.error("Студент с ID: {} не найден", studentId);
            throw new UserExistException("Student not found with id - " + studentId);
        }
    }

    public List<StudentResponse> getAllStudents() {
        LOGGER.info("Получение всех студентов");
        return studentUserRepository.findAllResponse();
    }

    public List<StudentDTOForAll> findAllStudents() {
        LOGGER.info("Получение всех студентов DTO");
        return studentUserRepository.findAllStudentDTOs();
    }

    public StudentDTO findStudentById(Long studentId) {
        LOGGER.info("Получение студента с ID: {}", studentId);

        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Стажер с ID: " + studentId + " не найден"));

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setImage(student.getImage());
        studentDTO.setFirstname(student.getFirstName());
        studentDTO.setLastname(student.getLastName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setStatus(student.getStatus());

        if (student.getDirection() != null) {
            studentDTO.setDirection(student.getDirection().getName());
        }

        List<String> projectNames = student.getProjects().stream()
                .map(Project::getTitle)
                .collect(Collectors.toList());
        studentDTO.setProjects(projectNames);

        return studentDTO;
    }

    public Student getStudentById(Long studentId) {
        LOGGER.info("Получение студента сущности с ID: {}", studentId);
        return studentUserRepository.findUserById(studentId)
                .orElseThrow(() -> new NotFoundException("Student cannot be found"));
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        LOGGER.info("Удаление студента с ID: {}", studentId);
        Student student = getStudentById(studentId);
        Set<Project> projects = student.getProjects();
        List<Project> updatedProjects = new ArrayList<>();
        projects.forEach(
                project -> {
                    project.getStudents().remove(student);
                    updatedProjects.add(project);
                }
        );
        projectRepository.saveAll(updatedProjects);
        studentUserRepository.delete(student);
        LOGGER.info("Студент с ID: {} успешно удалён", studentId);
    }

    public StudentResponse findById(Long studentId) {
        LOGGER.info("Поиск студента по ID: {}", studentId);
        return studentUserRepository.findByIdResponse(studentId);
    }

    public StudentResponse registerStudent(String email,
                                           Integer code,
                                           String password) {
        LOGGER.info("Регистрация студента с email: {}", email);

        Student student = studentUserRepository.findByEmail(email).orElseThrow(
                () -> {
                    LOGGER.error("Студент с email: {} не найден", email);
                    return new EntityNotFoundException("Студент с email: " + email + " не найден");
                });

        if (student.isRegistered()) {
            LOGGER.error("Ссылка уже была использована для студента с email: {}", email);
            throw new RuntimeException("This link has already been used");
        }

        if (!Objects.equals(student.getCode(), code) || code.equals(0)) {
            LOGGER.error("Некорректная ссылка для студента с email: {}", email);
            throw new RuntimeException("This is not a correct link");
        }

        User user = new User(student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                passwordEncoder.encode(password),
                ERole.ROLE_STUDENT, LocalDateTime.now());

        userRepository.save(user);

        student.setCode(0);
        student.setRegistered(true);
        student.setUser(user);
        studentUserRepository.save(student);

        LOGGER.info("Студент с email: {} успешно зарегистрирован", email);
        return findById(student.getId());
    }

    @Transactional
    public StudentResponse updateStudentStatus(Long id, Status newStatus) {
        try {
            LOGGER.info("Попытка обновить статус студента с ID: {}", id);

            Student student = getStudentById(id);

            LOGGER.debug("Текущий статус: {}, Новый статус: {}", student.getStatus(), newStatus);

            student.setStatus(newStatus);
            Student updatedStudent = studentUserRepository.save(student);

            LOGGER.info("Статус студента с ID: {} успешно обновлён", id);

            return new StudentResponse(
                    updatedStudent.getId(),
                    updatedStudent.getImage(),
                    updatedStudent.getFirstName(),
                    updatedStudent.getLastName(),
                    updatedStudent.getEmail(),
                    updatedStudent.getPhoneNumber(),
                    updatedStudent.getDirection().getName(),
                    updatedStudent.getStatus()
            );

        } catch (EntityNotFoundException e) {
            LOGGER.error("Ошибка обновления статуса: Студент с ID {} не найден", id, e);
            throw e;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка обновления статуса: Недопустимый аргумент для студента с ID: {}", id, e);
            throw e;

        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при обновлении статуса студента с ID: {}", id, e);
            throw new RuntimeException("Не удалось обновить статус студента из-за непредвиденной ошибки.", e);
        }
    }

    public StudentDTO addPointForStudent(Long studentId, Integer point) {
        LOGGER.info("Добавление баллов студенту с ID: {}", studentId);

        Student student = studentUserRepository.findById(studentId).orElseThrow(
                () -> {
                    LOGGER.error("Студент с ID: {} не найден", studentId);
                    return new NotFoundException("Not found student ID: " + studentId);
                });

        student.setPoint(point);
        studentUserRepository.save(student);
        LOGGER.info("Баллы добавлены студенту с ID: {}", studentId);
        return findByIdStudentInfo(studentId);
    }

    public StudentDTO findByIdStudentInfo(Long studentId) {
        LOGGER.info("Поиск информации о студенте с ID: {}", studentId);

        studentUserRepository.findById(studentId).orElseThrow(
                () -> {
                    LOGGER.error("Студент с ID: {} не найден", studentId);
                    return new NotFoundException("Not found student ID: " + studentId);
                });

        StudentDTO dto = studentUserRepository.findByIdStudentDTO(studentId);
        dto.setProjects(projectRepository.findTitlesByStudentId(studentId));
        LOGGER.info("Информация о студенте с ID: {} успешно найдена", studentId);
        return dto;
    }
}