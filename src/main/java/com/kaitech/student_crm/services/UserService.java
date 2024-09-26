package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.UserDTO;
import com.kaitech.student_crm.dtos.UserResponse;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.payload.request.LoginRequest;
import com.kaitech.student_crm.payload.request.SignUpRequest;
import com.kaitech.student_crm.payload.response.JWTTokenSuccessResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.repositories.UserRepository;
import com.kaitech.student_crm.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ResourceLoader resourceLoader;
    @Value("${linkReset}")
    private String linkForReset;


    public ResponseEntity<Object> signIn(LoginRequest loginRequest) {
        LOGGER.info("Попытка входа пользователя с email: {}", loginRequest.getEmail());

        if (userRepository.findUserByEmail(loginRequest.getEmail()).isEmpty()) {
            LOGGER.warn("Email {} не найден", loginRequest.getEmail());
            return new ResponseEntity<>(new MessageResponse("Email not found"), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findUserByEmail(loginRequest.getEmail()).get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            LOGGER.warn("Неверный пароль для email: {}", loginRequest.getEmail());
            return new ResponseEntity<>(new MessageResponse("Incorrect password"), HttpStatus.BAD_REQUEST);
        }
        String jwt = jwtUtils.generateToken(loginRequest.getEmail());

        LOGGER.info("Вход успешен для пользователя с email: {}", loginRequest.getEmail());
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    public UserResponse newPassword(String email, Integer code, String password) {
        LOGGER.info("Обновление пароля для пользователя с email: {}", email);

        User user = userRepository.findUserByEmail(email).orElseThrow(() -> {
            LOGGER.error("Пользователь с email {} не найден", email);
            return new UsernameNotFoundException("User with email " + email + " not found");
        });

        if (!Objects.equals(user.getCode(), code) || code.equals(0)) {
            LOGGER.error("Некорректный код для пользователя с email: {}", email);
            throw new RuntimeException("This is not a correct link");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setCode(0);
        userRepository.save(user);

        LOGGER.info("Пароль успешно обновлён для пользователя с email: {}", email);
        return userRepository.findByIdResponse(user.getId());
    }

    public MessageResponse resetPassword(String email) {
        LOGGER.info("Запрос на сброс пароля для email: {}", email);

        if (userRepository.findUserByEmail(email).isEmpty()) {
            LOGGER.warn("Email {} не найден", email);
            return new MessageResponse("Not found email");
        }

        User user = userRepository.findUserByEmail(email).orElseThrow();
        Random random = new Random();
        Integer randomCode = random.nextInt(9000) + 1000;
        user.setCode(randomCode);
        userRepository.save(user);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true для возможности работы с вложениями
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(email);
            helper.setSubject("Код для сброса пароля");
            helper.setText(htmlContent().replace("{code}", String.valueOf(randomCode)),
                    true); // true для того, чтобы указать, что это HTML-содержимое

            javaMailSender.send(message);
            LOGGER.info("Ссылка для сброса пароля отправлена на email: {}", email);
        } catch (MailException e) {
            LOGGER.error("Ошибка при отправке письма на email: {}", email, e);
            throw new RuntimeException("Please enter a valid email address.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return new MessageResponse("The link has been sent to your email.");
    }

    public ResponseEntity<Object> createUser(SignUpRequest user) {
        LOGGER.info("Попытка создания нового пользователя с email: {}", user.getEmail());

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            LOGGER.warn("Email {} уже существует", user.getEmail());
            return new ResponseEntity<>(new MessageResponse("Email found. Email must be unique"), HttpStatus.BAD_REQUEST);
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            LOGGER.warn("Пароли не совпадают для email: {}", user.getEmail());
            return new ResponseEntity<>(new MessageResponse("Password mismatch"), HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(ERole.ROLE_ADMIN);

        try {
            LOGGER.info("Сохранение нового пользователя с email: {}", user.getEmail());
            userRepository.save(newUser);
        } catch (Exception e) {
            LOGGER.error("Ошибка при регистрации пользователя с email: {}", user.getEmail(), e);
            return new ResponseEntity<>(new MessageResponse("The user " + newUser.getUsername() + " already exists"), HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Пользователь с email: {} успешно зарегистрирован", user.getEmail());
        return new ResponseEntity<>(userRepository.findByIdResponse(newUser.getId()), HttpStatus.OK);
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        LOGGER.info("Обновление данных пользователя по principal: {}", principal.getName());

        User user = getUserByPrincipal(principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());

        User updatedUser = userRepository.save(user);
        LOGGER.info("Данные пользователя с email: {} успешно обновлены", updatedUser.getEmail());
        return updatedUser;
    }

    public User getCurrentUser(Principal principal) {
        LOGGER.info("Получение текущего пользователя по principal: {}", principal.getName());
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> {
                    LOGGER.error("Пользователь с username: {} не найден", username);
                    return new UsernameNotFoundException("Username not found with username " + username);
                });
    }

    public User getUserById(Long id) {
        LOGGER.info("Получение пользователя с ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Пользователь с ID: {} не найден", id);
                    return new UsernameNotFoundException("User not found");
                });
    }

    private String loadHtmlTemplate(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }

    public String htmlContent() {
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
                        <h1>Ваш код для обновление почты</h1>
                        <p>Данный код предназначен для обновление почты.
                            Пожалуйста, не делитесь этим кодом с другими людьми.</p>
                        <div class="code">{code}</div>
                    </div>
                </div>
                </body>
                </html>
                """;
    }
}