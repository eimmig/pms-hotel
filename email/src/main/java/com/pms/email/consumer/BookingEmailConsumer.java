package com.pms.email.consumer;


import com.pms.email.dto.EmailDTO;
import com.pms.email.model.EmailModel;
import com.pms.email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BookingEmailConsumer {
    final EmailService emailService;

    public BookingEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }


    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenBookingRoomQueue(@Payload EmailDTO emailDTO) throws Exception {
        var emailModel = new EmailModel();
        emailModel.setBookingId(emailDTO.bookingId());
        emailModel.setEmailTo(emailDTO.emailTo());
        emailModel.setSubject(emailDTO.subject());
        emailModel.setText(emailDTO.message());

        emailService.sendEmail(emailModel);
    }
}
