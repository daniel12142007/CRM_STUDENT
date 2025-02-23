package com.kaitech.student_crm.services;

import com.kaitech.student_crm.config.JwtUtils;
import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.StudentDTOForAll;
import com.kaitech.student_crm.exceptions.EmailAlreadyExistsException;
import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.*;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.models.enums.Status;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.request.StudentRegisterRequest;
import com.kaitech.student_crm.payload.request.StudentRequest;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import com.kaitech.student_crm.payload.response.LevelResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.repositories.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentUserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(StudentUserService.class);
    private final StudentUserRepository studentUserRepository;
    private final DirectionRepository directionRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;
    private final ArchiveRepository archiveRepository;
    private final LevelRepository levelRepository;
    private final NotificationRepository notificationRepository;
    private final S3FileService s3FileService;
    private final JwtUtils jwtUtils;
    @Value("${link}")
    private String link;
    @Value("${s3.viewImage}")
    private String linkViewImage;
    private final ResourceLoader resourceLoader;

    public StudentDTO createStudent(StudentDataRequest student,
                                    Status status,
                                    Long directionId,
                                    MultipartFile image) {
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
        newStudent.setFirstName(student.getFirstname());
        newStudent.setLastName(student.getLastname());
        newStudent.setCode(randomCode);
        if (image != null) {
            try {
                newStudent.setImage(linkViewImage + s3FileService.upload(image));
                LOGGER.info("Изображение успешно сохранено");
            } catch (IOException ignore) {
                LOGGER.error("Не удалось сохранить изображние");
            }
        }
        try {
            newStudent.setStatus(status);
            newStudent.setEmail(student.getEmail());
            newStudent.setDirection(directionRepository.findById(directionId).get());
            newStudent.setPhoneNumber(student.getPhoneNumber());

            String htmlContent = registered();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true для возможности работы с вложениями
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(newStudent.getEmail());
            helper.setSubject("Добро пожаловать!");
            helper.setText(htmlContent, true); // true для того, чтобы указать, что это HTML-содержимое

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Вы ввели неправильный адрес электронной почты", e);
        }
        try {
            LOGGER.info("Сохранение студента {}", student.getEmail());
            studentUserRepository.save(newStudent);
        } catch (
                Exception e) {
            LOGGER.error("Ошибка при регистрации, {}", e.getMessage());
            throw new UserExistException("The student " + newStudent.getFirstName() + " " + newStudent.getLastName() + " already exists");
        }

        return

                findByIdStudentInfo(newStudent.getId());
    }

    public StudentDTO updateStudent(Long studentId, StudentRequest request) {
        LOGGER.info("Обновление студента с ID: {}", studentId);

        Optional<Student> studentOptional = studentUserRepository.findById(studentId);

        String email;
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            email = student.getEmail();
            if (!student.getEmail().equals(request.email())) {
                boolean emailExists = studentUserRepository.existsByEmail(request.email());
                if (emailExists) {
                    LOGGER.error("Email {} уже используется", request.email());
                    throw new EmailAlreadyExistsException("Email " + request.email() + " is already in use");
                }
            }
            if (request.direction() != 0 && !studentOptional.get().getDirection().getId().equals(request.direction())) {
                Direction direction = directionRepository.findById(request.direction()).orElseThrow(
                        () -> new NotFoundException("Not found direction ID:" + request.direction())
                );
                try {
                    if (direction != student.getDirection()) {
                        Notification notification = Notification.builder()
                                .student(student)
                                .date(LocalDateTime.now())
                                .message("Ваша направление был изменен с " + student.getDirection().getName() + " на " + direction.getName())
                                .build();
                        notificationRepository.save(notification);
                    }
                } catch (Exception ignore) {
                }
                student.setDirection(direction);
            }
            User user = userRepository.findUserByEmail(student.getEmail()).orElseThrow(
                    () -> new NotFoundException("Not Found user email: " + email)
            );
            user.setEmail(request.email());
            user.setFirstname(request.firstName());
            user.setLastname(request.lastName());
            student.setFirstName(request.firstName());
            student.setLastName(request.lastName());
            student.setEmail(request.email());
            student.setPhoneNumber(request.phoneNumber());
            student.setPoint(request.point());
            studentUserRepository.save(student);
            userRepository.save(user);
            addPointForStudent(studentId, request.point());
            LOGGER.info("Студент с ID: {} успешно обновлён", studentId);
            return findByIdStudentInfo(studentId);
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
        User user = student.getUser();
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
        if (user != null)
            userRepository.delete(user);
        LOGGER.info("Студент с ID: {} успешно удалён", studentId);
    }

    public StudentDTO registerStudent(StudentRegisterRequest request) {
        if (!request.passwordOne().equals(request.passwordTwo())) {
            LOGGER.error("Пароли не совпадают");
            throw new IllegalArgumentException("The passwords do not match");
        }
        String email = request.email();
        LOGGER.info("Регистрация студента с email: {}", email);

        Student student = studentUserRepository.findByEmail(email).orElseThrow(
                () -> {
                    LOGGER.error("Студент с email: {} не найден", email);
                    return new AccessDeniedException("Для студента email: " + email + " доступа нету");
                });

        LOGGER.info("Поиск direction по ID: {}", request.directionId());
        Direction direction = directionRepository.findById(request.directionId()).orElseThrow(
                () -> {
                    LOGGER.error("Direction с id: {} не найден", request.directionId());
                    return new NotFoundException("Direction c ID: " + request.directionId() + " не найден");
                }
        );

        if (student.isRegistered()) {
            LOGGER.error("Ссылка уже была использована для студента с email: {}", email);
            throw new RuntimeException("This link has already been used");
        }
        User user = new User(student.getFirstName(),
                request.lastName(),
                email,
                passwordEncoder.encode(request.passwordOne()),
                ERole.ROLE_STUDENT, LocalDateTime.now());

        userRepository.save(user);

        student.setUser(user);
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setDirection(direction);
        student.setCode(0);
        student.setRegistered(true);
        studentUserRepository.save(student);

        LOGGER.info("Студент с email: {} успешно зарегистрирован", email);
        return findByIdStudentInfo(student.getId());
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
        LOGGER.info("Запрос на добавление баллов студенту с ID: {} и баллами: {}", studentId, point);

        Student student = studentUserRepository.findById(studentId).orElseThrow(
                () -> {
                    LOGGER.error("Студент с ID: {} не найден", studentId);
                    return new NotFoundException("Not found student ID: " + studentId);
                });

        LOGGER.debug("Найден студент: {}", student);

        Level level = levelRepository.findBetweenPointFrontAndPointTo(point).orElse(
                levelRepository.findLevelIfNull(point)
        );
        if (level == null) {
            level = new Level("Not at the level yet", 0, 0, "The student has no level");
            LOGGER.warn("Не удалось найти уровень для баллов: {}. Установлен уровень по умолчанию.", point);
        } else {
            LOGGER.debug("Найден уровень: {}", level);
        }

        final Integer oldPoint = student.getPoint();

        Archive archive;

        if (!level.getTitle().equals("Not at the level yet")) {
            student.setLevel(level);
            LOGGER.info("Обновлен уровень студента с ID: {} на {}", studentId, level);
        }
        student.setPoint(point);
        LOGGER.info("Установлены новые баллы: {} для студента с ID: {}", point, studentId);

        if (student.getLevel() == null) {
            archive = new Archive(student, level);
            LOGGER.debug("Создан архив для студента: {}", archive);
        } else {
            Level oldLevel = levelRepository.findBetweenPointFrontAndPointTo(oldPoint).orElse(
                    levelRepository.findLevelIfNull(oldPoint)
            );
            if (oldLevel == null) {
                oldLevel = new Level("Not at the level yet", 0, 0, "The student has no level");
                LOGGER.warn("Не удалось найти старый уровень для баллов: {}. Установлен уровень по умолчанию.", oldPoint);
            }
            archive = new Archive(student, oldPoint, level, oldLevel);
            LOGGER.debug("Создан архив с изменением уровня: {}", archive);
        }

        studentUserRepository.save(student);
        Notification notification = Notification.builder()
                .student(student)
                .date(LocalDateTime.now())
                .message("Ваш уровень был изменен с " + archive.getOldLevel() + " на " + archive.getNewLevel() + ". Ваш старый балл — " + oldPoint + ", а новый — " + point)
                .build();
        LOGGER.info("Сохранен студент с ID: {}", studentId);

        archiveRepository.save(archive);
        notificationRepository.save(notification);
        LOGGER.info("Сохранен архив и уведомление для студента с ID: {}", studentId);

        LOGGER.info("Баллы успешно добавлены студенту с ID: {}", studentId);
        return findByIdStudentInfo(studentId);
    }


    public StudentDTO findByIdStudentInfo(Long studentId) {
        LOGGER.info("Поиск информации о студенте с ID: {}", studentId);
        StudentDTO dto = studentUserRepository.findByIdStudentDTO(studentId).orElseThrow(
                () -> {
                    LOGGER.error("Студент с ID: {} не найден", studentId);
                    return new NotFoundException("Not found student ID: " + studentId);
                });
        dto.setProjects(projectRepository.findTitlesByStudentId(studentId));
        if (dto.getLevel() == null)
            dto.setLevel(studentUserRepository.findLevelIfNull(dto.getPoint(), dto.getId()));
        LOGGER.info("Информация о студенте с ID: {} успешно найдена", studentId);
        return dto;
    }

    public StudentDTO deleteImage(String email, Long studentId) {
        LOGGER.info("Попытка удалить изображение студента. ID студента: {}, Email пользователя: {}", studentId, email);
        Student student = studentUserRepository.findById(studentId).orElseThrow(
                () -> {
                    LOGGER.error("Студент с ID {} не найден", studentId);
                    return new NotFoundException("Not found student ID : " + studentId);
                }
        );
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> {
                    LOGGER.error("Пользователь с email {} не найден", email);
                    return new NotFoundException("Not found user email : " + email);
                }
        );
        student.setImage(null);
        if (user.getRole().equals(ERole.ROLE_ADMIN) || student.getEmail().equals(email)) {
            LOGGER.info("Пользователь имеет права для изменения изображения или является владельцем аккаунта.");
            studentUserRepository.save(student);
            LOGGER.info("Изображение студента с ID {} успешно удалено", studentId);
        } else {
            LOGGER.warn("Попытка изменения изображения студента с ID {} от пользователя с Email {} без достаточных прав", studentId, email);
            throw new AccessDeniedException("You do not have access to change the student's photo, you must be an admin or the owner of this account");
        }
        LOGGER.info("Возвращение информации о студенте с ID {}", studentId);
        return findByIdStudentInfo(studentId);
    }

    @Transactional
    public StudentResponse updateLevel(Long studentId, Long newLevelId) {
        try {
            LOGGER.info("Начинается обновление уровня для студента с ID: {}", studentId);

            Student student = studentUserRepository.findById(studentId)
                    .orElseThrow(() -> {
                        LOGGER.error("Студент с ID {} не найден", studentId);
                        return new NotFoundException("Студент не найден");
                    });

            Level newLevel = levelRepository.findById(newLevelId)
                    .orElseThrow(() -> {
                        LOGGER.error("Уровень с ID {} не найден", newLevelId);
                        return new NotFoundException("Уровень не найден");
                    });

            Level oldLevel = student.getLevel();
            LOGGER.info("Обновление уровня студента с ID {} с {} на {}", studentId, oldLevel != null ? oldLevel.getTitle() : "нет уровня", newLevel.getTitle());

            student.setLevel(newLevel);

            Archive archive = new Archive();
            archive.setStudent(student);
            archive.setNewLevel(newLevel.getTitle());
            archive.setOldLevel(oldLevel != null ? oldLevel.getTitle() : null);
            archive.setDateUpdate(LocalDateTime.now());
            archive.setFirstName(student.getFirstName());
            archive.setLastName(student.getLastName());
            archive.setImage(student.getImage());

            archiveRepository.save(archive);
            LOGGER.info("Архивная запись успешно создана для студента с ID: {}", studentId);

            Student updatedStudent = studentUserRepository.save(student);
            LOGGER.info("Уровень студента с ID {} успешно обновлен", studentId);

            LevelResponse levelResponse = new LevelResponse(newLevel.getId(), newLevel.getTitle());
            return new StudentResponse(
                    updatedStudent.getId(),
                    updatedStudent.getImage(),
                    updatedStudent.getFirstName(),
                    updatedStudent.getLastName(),
                    updatedStudent.getEmail(),
                    updatedStudent.getPhoneNumber(),
                    updatedStudent.getDirection().getName(),
                    updatedStudent.getStatus(),
                    levelResponse
            );

        } catch (NotFoundException e) {
            LOGGER.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Неизвестная ошибка при обновлении уровня студента с ID: {}", studentId, e);
            throw new RuntimeException("Произошла ошибка при обновлении уровня", e);
        }
    }

    public List<StudentResponse> filterStudentsByLevel(Long levelId) {
        List<StudentResponse> studentResponses;
        try {
            LOGGER.info("Начало фильтрации студентов по уровню с id: {}", levelId);
            List<Student> students = studentUserRepository.findByLevelId(levelId);
            studentResponses = convertToStudentResponse(students);
            LOGGER.info("Фильтрация студентов завершена успешно. Найдено студентов: {}", studentResponses.size());
        } catch (Exception e) {
            LOGGER.error("Ошибка при фильтрации студентов по уровню с id: {}", levelId, e);
            throw new RuntimeException("Ошибка при фильтрации студентов", e);
        }
        return studentResponses;
    }

    private List<StudentResponse> convertToStudentResponse(List<Student> students) {
        List<StudentResponse> studentResponses = new ArrayList<>();
        for (Student student : students) {
            LevelResponse levelResponse = new LevelResponse(
                    student.getLevel().getId(),
                    student.getLevel().getTitle(),
                    student.getLevel().getDescription(),
                    student.getLevel().getPointFrom(),
                    student.getLevel().getPointTo()
            );
            StudentResponse studentResponse = new StudentResponse(
                    student.getId(),
                    student.getImage(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhoneNumber(),
                    student.getStatus(),
                    levelResponse
            );
            studentResponses.add(studentResponse);
        }
        return studentResponses;
    }

    public List<StudentResponse> filterByDirection(String directionName) {
        LOGGER.info("Фильтрация студентов по направлению: {}", directionName);

        Direction direction = directionRepository.findByName(directionName)
                .orElseThrow(() -> new NotFoundException("Направление с именем: " + directionName + " не найден"));

        List<Student> students = studentUserRepository.findByDirection(direction);

        List<StudentResponse> studentResponses = convertToStudentResponse(students);

        LOGGER.info("Найдено {} студентов для направления: {}", studentResponses.size(), directionName);
        return studentResponses;
    }

    public List<StudentResponse> filterByProject(Long projectId) {
        LOGGER.info("Фильтрация студента по проекту: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Проект с ID: " + projectId + " не найден"));

        List<Student> students = studentUserRepository.findByProjects(project);

        if (students.isEmpty()) {
            LOGGER.warn("Не найдено ни одного студента для  проекта с ID: {}", projectId);
            throw new NotFoundException("Для указанного проекта не найдено ни одного студента");
        }

        return convertToStudentResponse(students);
    }


    public StudentDTO updateImage(Long studentId,
                                  String email,
                                  MultipartFile image) {

        LOGGER.info("Попытка обновить изображение студента. ID студента: {}, Email пользователя: {}", studentId, email);
        Student student = studentUserRepository.findById(studentId).orElseThrow(
                () -> {
                    LOGGER.error("Студент с ID {} не найден ", studentId);
                    return new NotFoundException("Not found student ID : " + studentId);
                }
        );
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> {
                    LOGGER.error("Пользователь с email {} не найден ", email);
                    return new NotFoundException("Not found user email : " + email);
                }
        );
        try {
            student.setImage(linkViewImage + s3FileService.upload(image));
            LOGGER.info("Изображение успешно обновлено");
        } catch (IOException ignore) {
            LOGGER.error("Не удалось обновить изображние");
        }
        if (user.getRole().equals(ERole.ROLE_ADMIN) || student.getEmail().equals(email)) {
            LOGGER.info("Пользователь имеет права для изменения изображения или является владельцем аккаунта. ");
            studentUserRepository.save(student);
            LOGGER.info("Изображение студента с ID {} успешно обновлено", studentId);
        } else {
            LOGGER.warn("Попытка изменения изображения студента с ID {} от пользователя с Email {} без достаточных прав", studentId, email);
            throw new AccessDeniedException("You do not have access to change the student's photo, you must be an admin or the owner of this account");
        }
        LOGGER.info("Возвращение информации о студенте с ID {}", studentId);
        return findByIdStudentInfo(studentId);
    }


    public List<StudentResponse> findStudentsByName(String name) {
        LOGGER.info("Поиск студентов по имени: {}", name);
        return studentUserRepository.findStudentByName(name);
    }

    public StudentResponse findStudentByEmail(String email) {
        LOGGER.info("Поиск студента по email: {}", email);
        return studentUserRepository.findStudentByEmail(email)
                .orElseThrow(() -> new NotFoundException("Student not found with email: " + email));
    }

    public MessageResponse sendVerificationCode(String email, String newEmail) {
        LOGGER.info("Запрос на изменение email для пользователя с текущим email: {}", email);

        Optional<Student> studentOpt = studentUserRepository.findByEmail(email);

        if (studentOpt.isEmpty()) {
            LOGGER.warn("Пользователь с email {} не найден", email);
            return new MessageResponse("Пользователь с текущим email не найден.");
        }

        Student student = studentOpt.get();

        Random random = new Random();
        int verificationCode = 1000 + random.nextInt(9000);
        student.setCode(verificationCode);

        studentUserRepository.save(student);

        try {
            String htmlContent = updateEmail();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true для возможности работы с вложениями
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(newEmail);
            helper.setSubject("Код для обновления email");
            helper.setText(htmlContent.replace("{code}", String.valueOf(verificationCode)),
                    true); // true для того, чтобы указать, что это HTML-содержимое

            javaMailSender.send(message);
            LOGGER.info("Письмо с кодом подтверждения отправлено на новый email: {}", newEmail);
        } catch (MailException e) {
            LOGGER.error("Ошибка при отправке письма на email: {}", newEmail, e);
            throw new RuntimeException("Не удалось отправить письмо с кодом подтверждения. Пожалуйста, введите корректный адрес электронной почты.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return new MessageResponse("Код подтверждения отправлен на ваш новый email: " + newEmail);
    }

    public String verifyCodeAndChangeEmail(String email, int inputCode, String newEmail) {
        Optional<Student> studentOpt = studentUserRepository.findByEmail(email);
        User user = userRepository.findUserByEmail(email).orElseThrow();
        user.setEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            if (studentUserRepository.findByEmail(newEmail).isPresent()) {
                return "Новый email уже используется. Пожалуйста, используйте другой email.";
            }

            if (student.getCode() != null && student.getCode() == inputCode) {
                student.setEmail(newEmail);
                student.setCode(0);
                studentUserRepository.save(student);
                userRepository.save(user);
                return "Email успешно изменен на " + newEmail;
            } else {
                return "Неверный код подтверждения!";
            }
        } else {
            return "Пользователь с текущим email не найден.";
        }
    }

    public Optional<StudentResponse> getStudentProfileByEmail(String email) {
        Optional<StudentResponse> optionalStudent = studentUserRepository.findStudentByEmail(email);

        if (optionalStudent.isEmpty()) {
            return Optional.empty();
        }

        StudentResponse student = optionalStudent.get();

        List<String> projectTitles = projectRepository.findTitlesByStudentId(student.id());

        List<ProjectResponse> projectResponses = projectTitles.stream()
                .map(title -> new ProjectResponse(null, title, null, null, null, null))  // Заполняем только поле title
                .toList();

        Optional<String> levelTitle = levelRepository.findLevelByStudentId(student.id());


        LevelResponse levelResponse = levelTitle.map(title -> new LevelResponse(null, title, null, null, null))
                .orElse(null);

        return Optional.of(new StudentResponse(
                student.id(),
                student.image(),
                student.firstName(),
                student.lastName(),
                student.email(),
                student.phoneNumber(),
                student.direction(),
                projectResponses,
                student.status(),
                levelResponse
        ));
    }


    @Transactional
    public StudentResponse updateStudentDetails(String email, String firstName, String lastName, String phoneNumber) {
        Optional<Student> optionalStudent = studentUserRepository.findEntityByEmail(email);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setPhoneNumber(phoneNumber);

            studentUserRepository.save(student);

            return new StudentResponse(
                    student.getId(),
                    student.getImage(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhoneNumber()


            );

        } else {
            throw new EntityNotFoundException("Student not found with email: " + email);
        }
    }

    private String updateEmail() {
        return """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Сброс пароля</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            background-color: #f2f2f2;
                        }
                
                        .email-container {
                            width: 780px;
                            height: 600px;
                            background-color: #fff;
                            border: 1px solid #ddd;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        }
                
                        .header {
                            background-color: #407BFF;
                            color: #fff;
                            text-align: left;
                            padding: 20px;
                            font-size: 24px;
                            font-weight: bold;
                        }
                
                        .content {
                            padding: 40px;
                            text-align: center;
                        }
                
                        .content h1 {
                            font-size: 24px;
                            color: #333;
                        }
                
                        .content p {
                            font-size: 16px;
                            color: #555;
                            line-height: 1.5;
                        }
                
                        .code {
                            font-size: 48px;
                            font-weight: bold;
                            margin: 30px 0;
                            color: #333;
                        }
                    </style>
                </head>
                <body>
                <div class="email-container">
                    <div class="header">
                        KaiTech
                    </div>
                    <div class="content">
                        <h1>Ваша письмо для сброса пароля</h1>
                        <p>Данный код предназначен для сброса пароля.
                            Пожалуйста, не делитесь этим кодом с другими людьми, так как это может привести к
                            несанкционированному доступу к вашему аккаунту.</p>
                        <div class="code">{code}</div>
                    </div>
                </div>
                </body>
                </html>
                """;
    }

    private String registered() {
        return """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Сброс пароля</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      margin: 0;
                      padding: 0;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      height: 100vh;
                      background-color: #f2f2f2;
                    }
                
                    .email-container {
                      width: 780px;
                      height: 600px;
                      background-color: #fff;
                      border: 1px solid #ddd;
                      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    }
                
                    .header {
                      background-color: #407BFF;
                      color: #fff;
                      text-align: left;
                      padding: 20px;
                      font-size: 24px;
                      font-weight: bold;
                    }
                
                    .content {
                      padding: 40px;
                      text-align: center;
                    }
                
                    .content h1 {
                      font-size: 24px;
                      color: #333;
                    }
                
                    .content p {
                      font-size: 16px;
                      color: #555;
                      line-height: 1.5;
                    }
                
                  </style>
                </head>
                <body>
                <div class="email-container">
                  <div class="header">
                    KaiTech
                  </div>
                  <div class="content">
                    <h1>Добро пожаловать!
                      Вы успешно получили доступ для регистрации на сайте CRM.</h1>
                  </div>
                </div>
                </body>
                </html>
                """;
    }
}