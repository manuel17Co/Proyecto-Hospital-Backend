package com.meditech.hospital.common.email

import java.util.List;

import org.springframework.stereotype.Component;

import com.meditech.hospital.common.email.dto.ChangePasswordEmailRequestDto;
import com.meditech.hospital.common.email.dto.VerificationEmailRequestDto;
import com.meditech.hospital.common.email.dto.WelcomeEmailRequestDto;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

@Component
public class EmailSender {

    private static EmailSender instance;

    private final String apiKey;

    private final MailtrapClient client;

    private EmailSender() {
        this.apiKey = System.getenv("EMAIL_TOKEN");
        final MailtrapConfig config = new MailtrapConfig.Builder()
            .token(apiKey)
            .build();

        this.client = MailtrapClientFactory.createMailtrapClient(config);
    }

    public static EmailSender getInstance() {
        if (instance == null) {
            instance = new EmailSender();
        }
        return instance;
    }

    public void sendWelcomeEmail(String toEmail, WelcomeEmailRequestDto variables) {
        final MailtrapMail mail = MailtrapMail.builder()
            .from(new Address("support-canchafacil@pablouribezuluaga.com", "CanchaFacil Support"))
            .to(List.of(new Address(toEmail)))
            .templateUuid("65eb20cd-3be5-48f0-bd3e-ba5caea25965")
            .templateVariables(variables.getAll())
            .build();
        try {
            client.send(mail);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, VerificationEmailRequestDto variables) {
        final MailtrapMail mail = MailtrapMail.builder()
            .from(new Address("support-canchafacil@pablouribezuluaga.com", "CanchaFacil Support"))
            .to(List.of(new Address(toEmail)))
            .templateUuid("a051b6f3-edd9-4f12-acfd-38b627747eb2")
            .templateVariables(variables.getAll())
            .build();
        try {
            client.send(mail);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    public void sendChangePasswordEmail(String toEmail, ChangePasswordEmailRequestDto variables) {
        final MailtrapMail mail = MailtrapMail.builder()
            .from(new Address("support-canchafacil@pablouribezuluaga.com", "CanchaFacil Support"))
            .to(List.of(new Address(toEmail)))
            .templateUuid("d21f4531-a3c4-4dd5-b94d-fd59fbe63889")
            .templateVariables(variables.getAll())
            .build();
        try {
            client.send(mail);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}
