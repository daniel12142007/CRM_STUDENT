package com.kaitech.student_crm.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;

    @GetMapping("test/old/{email}")
    public String sendEmail(@PathVariable String email) throws MessagingException, IOException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@baeldung.com");
            message.setTo(email);
            message.setSubject("Hello");
            message.setText("Ответ на ваще обращение");
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Вы вели не правильную почту");
        }
        return "Success";
    }

    @GetMapping("test/new/{email}")
    public String sendEmailNew(@PathVariable String email) throws MessagingException, IOException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true для возможности работы с вложениями
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(email);
            helper.setSubject("Hello");

            String htmlContent = loadHtmlTemplate("classpath:static-html/resetPassword.html");
            helper.setText(htmlContent, true); // true для того, чтобы указать, что это HTML-содержимое

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Вы ввели неправильный адрес электронной почты", e);
        }
        return "Success";
    }

    private String loadHtmlTemplate(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }
}