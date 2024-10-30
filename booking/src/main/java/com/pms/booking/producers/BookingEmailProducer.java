package com.pms.booking.producers;

import com.pms.booking.dto.BookingEmailDTO;
import com.pms.booking.dto.BookingRoomDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookingEmailProducer {
    final RabbitTemplate rabbitTemplate;

    public BookingEmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageBookingRoom(BookingEmailDTO bookingEmailDTO) {
        rabbitTemplate.convertAndSend("", routingKey, bookingEmailDTO);
    }
}
