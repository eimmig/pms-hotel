package com.pms.booking.controller;

import com.pms.booking.dto.AmenitiesRequestDTO;
import com.pms.booking.model.AmenitiesModel;
import com.pms.booking.service.AmenitiesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AmenitiesControllerTest {

    @Mock
    private AmenitiesService amenitiesService;

    @InjectMocks
    private AmenitiesController amenitiesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() throws Exception {
        AmenitiesRequestDTO requestDTO = new AmenitiesRequestDTO(
                UUID.randomUUID(),
                "WiFi",
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10)
        );

        AmenitiesModel model = new AmenitiesModel();
        model.setId(UUID.randomUUID());
        model.setName(requestDTO.name());

        when(amenitiesService.save(any(AmenitiesRequestDTO.class))).thenReturn(model);

        ResponseEntity<AmenitiesModel> response = amenitiesController.save(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(model, response.getBody());
        verify(amenitiesService, times(1)).save(any(AmenitiesRequestDTO.class));
    }

    @Test
    void testGetAll() {
        AmenitiesModel model = new AmenitiesModel(UUID.randomUUID(), "WiFi");
        when(amenitiesService.getAll()).thenReturn(Collections.singletonList(model));

        ResponseEntity<List<AmenitiesModel>> response = amenitiesController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("WiFi", response.getBody().get(0).getName());
        verify(amenitiesService, times(1)).getAll();
    }
}
