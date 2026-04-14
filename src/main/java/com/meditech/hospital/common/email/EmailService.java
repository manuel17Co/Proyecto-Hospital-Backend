package com.meditech.hospital.common.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.meditech.hospital.common.email.dto.ChangePasswordEmailRequestDto;
import com.meditech.hospital.common.email.dto.VerificationEmailRequestDto;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, VerificationEmailRequestDto variables) {
        String htmlContent = String.format(
            "<h1>Hola %s</h1><p>Tu código de verificación para %s es: <b>%s</b></p><p>Expira en %s minutos.</p><p>© %s</p>",
            variables.getUsername(), variables.getAppName(), variables.getCode(), variables.getExpirationMinutes(), variables.getYear()
        );
        sendHtmlEmail(toEmail, "Verifica tu cuenta", htmlContent);
    }

    public void sendChangePasswordEmail(String toEmail, ChangePasswordEmailRequestDto variables) {
        String htmlContent = String.format(
            "<h1>Recuperación de contraseña</h1><p>Hola %s, usa este código: <b>%s</b></p><p>Válido por %s minutos.</p><p>© %s</p>",
            variables.getUsername(), variables.getCode(), variables.getExpirationMinutes(), variables.getYear()
        );
        sendHtmlEmail(toEmail, "Cambio de contraseña", htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("support@meditech-hospital.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // El true indica que es HTML

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }
    }
}