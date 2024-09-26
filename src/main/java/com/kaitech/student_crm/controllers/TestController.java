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
    public String sendEmail(@PathVariable String email) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ссылка для регистрации работает только 1 раз");
        javaMailSender.send(message);
        return "Success";
    }

    @GetMapping("test/new/{email}")
    public String sendEmailNew(@PathVariable String email) throws MessagingException, IOException {
        if (email != null) {
            return loadHtmlTemplate("classpath:static-html/registered.html");
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = loadHtmlTemplate("classpath:static-html/registered.html");

        helper.setFrom("kaitechcrm@gmail.com");
        helper.setSubject("Добро пожаловать!");
        helper.setTo(email);
        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
        return "Success";
    }

    private String loadHtmlTemplate(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }
}