package com.pms.email.service;

import com.pms.email.enums.StatusEmail;
import com.pms.email.model.EmailModel;
import com.pms.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendEmailSuccessfully() {
        // Dado
        EmailModel emailModel = new EmailModel();
        emailModel.setBookingId(UUID.randomUUID());
        emailModel.setEmailTo("test@example.com");
        emailModel.setSubject("Subject");
        emailModel.setText("Message");

        when(emailRepository.save(any(EmailModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Quando
        EmailModel result = emailService.sendEmail(emailModel);

        // Então
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
        assertEquals(StatusEmail.SENT, result.getStatusEmail());
        assertEquals(LocalDateTime.now().getDayOfYear(), result.getSendDate().getDayOfYear());
    }

    @Test
    void testSendEmailWithException() {
        // Dado
        EmailModel emailModel = new EmailModel();
        emailModel.setBookingId(UUID.randomUUID());
        emailModel.setEmailTo("test@example.com");
        emailModel.setSubject("Subject");
        emailModel.setText("Message");

        when(emailRepository.save(any(EmailModel.class))).thenReturn(emailModel);
        doThrow(new MailException("Mail send error") {}).when(emailSender).send(any(SimpleMailMessage.class));

        // Quando
        EmailModel result = emailService.sendEmail(emailModel);

        // Então
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
        assertEquals(StatusEmail.ERROR, result.getStatusEmail());
        assertEquals(emailModel.getBookingId(), result.getBookingId());
        assertEquals(emailModel.getEmailTo(), result.getEmailTo());
        assertEquals(emailModel.getSubject(), result.getSubject());
        assertEquals(emailModel.getText(), result.getText());
    }
}
