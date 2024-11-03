package com.pms.booking.controller;

import com.pms.booking.dto.BookingDTO;
import com.pms.booking.dto.BookingReceive;
import com.pms.booking.model.BookingModel;
import com.pms.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooking() {
        BookingReceive bookingReceive = new BookingReceive(
                UUID.randomUUID(),
                "2024-01-01",
                "2024-01-10",
                UUID.randomUUID(),
                "John Doe",
                "P",
                "PENDENTE",
                List.of()
        );

        when(bookingService.getAllBooking()).thenReturn(List.of(bookingReceive));

        List<BookingReceive> result = bookingController.getAllBooking();
        assertEquals(1, result.size());
        assertEquals(bookingReceive, result.get(0));
        verify(bookingService, times(1)).getAllBooking();
    }

    @Test
    void testSaveBooking() throws Exception {
        // Mock data
        BookingDTO bookingDTO = new BookingDTO(
                UUID.randomUUID(),
                12345L,
                new Date(),
                new Date(System.currentTimeMillis() + 86400000L),
                UUID.randomUUID(),
                "P",
                List.of()
        );
        BookingModel bookingModel = new BookingModel();
        bookingModel.setId(UUID.randomUUID());

        when(bookingService.save(any(BookingDTO.class))).thenReturn(bookingModel);

        ResponseEntity<BookingModel> response = bookingController.save(bookingDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bookingService, times(1)).save(any(BookingDTO.class));
    }
}
