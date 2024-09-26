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
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = loadHtmlTemplate("""
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
                """);

        helper.setFrom("kaitechcrm@gmail.com");
        helper.setSubject("Добро пожаловать!");
        helper.setTo(email);
        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
        return "Success";
    }

    @GetMapping("test/new/{email}")
    public String sendEmailNew(@PathVariable String email) throws MessagingException, IOException {
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