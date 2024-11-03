package com.pms.booking.controller;

import com.pms.booking.dto.PersonDTO;
import com.pms.booking.dto.PersonReciveDTO;
import com.pms.booking.model.PersonModel;
import com.pms.booking.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonControllerTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    private PersonModel personModel;
    private PersonDTO personDTO;

    @BeforeEach
    void setUp() {
        String dateString = "2001-01-01";

        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

        MockitoAnnotations.openMocks(this);
        personModel = new PersonModel();
        personModel.setId(UUID.randomUUID());
        personModel.setName("John Doe");
        personModel.setDocument("12345678909");
        personModel.setEmail("john.doe@example.com");

        personDTO = new PersonDTO(
                personModel.getId(),
                personModel.getName(),
                personModel.getDocument(),
                1, // Document type ID
                "1234567890",
                personModel.getEmail(),
                localDate
        );
    }

    @Test
    void testSave_Success() throws Exception {
        when(personService.save(any(PersonDTO.class))).thenReturn(personModel);

        ResponseEntity<PersonModel> response = personController.save(personDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(personModel, response.getBody());
    }

    @Test
    void testGetAllBooking() {
        List<PersonReciveDTO> bookingList = new ArrayList<>();
        bookingList.add(new PersonReciveDTO(personModel.getId(), personModel.getName()));
        when(personService.getAllBooking()).thenReturn(bookingList);

        ResponseEntity<List<PersonReciveDTO>> response = personController.getAllBooking();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingList, response.getBody());
    }
}
