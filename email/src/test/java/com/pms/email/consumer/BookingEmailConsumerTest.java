package com.pms.email.consumer;

import com.pms.email.consumer.BookingEmailConsumer;
import com.pms.email.dto.EmailDTO;
import com.pms.email.enums.StatusEmail;
import com.pms.email.model.EmailModel;
import com.pms.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingEmailConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingEmailConsumer bookingEmailConsumer;

    @Test
    void testListenBookingRoomQueue() throws Exception {
        UUID bookingId = UUID.randomUUID();
        EmailDTO emailDTO = new EmailDTO(bookingId, "test@example.com", "Subject", "Message");

        // Quando
        bookingEmailConsumer.listenBookingRoomQueue(emailDTO);

        // Então
        ArgumentCaptor<EmailModel> emailModelCaptor = ArgumentCaptor.forClass(EmailModel.class);
        verify(emailService, times(1)).sendEmail(emailModelCaptor.capture());

        EmailModel capturedEmailModel = emailModelCaptor.getValue();
        assertEquals(bookingId, capturedEmailModel.getBookingId());
        assertEquals("test@example.com", capturedEmailModel.getEmailTo());
        assertEquals("Subject", capturedEmailModel.getSubject());
        assertEquals("Message", capturedEmailModel.getText());
        assertNull(capturedEmailModel.getSendDate());
    }
}
