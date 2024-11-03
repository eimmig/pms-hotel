package com.pms.booking.controller;

import com.pms.booking.dto.RoomTypeDTO;
import com.pms.booking.model.RoomTypeModel;
import com.pms.booking.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomTypeControllerTest {

    @InjectMocks
    private RoomTypeController roomTypeController;

    @Mock
    private RoomTypeService roomTypeService;

    private RoomTypeModel roomTypeModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomTypeModel = new RoomTypeModel();
        roomTypeModel.setId(UUID.randomUUID());
        roomTypeModel.setName("Deluxe");
        roomTypeModel.setMaxPersons(2);
        roomTypeModel.setAbbreviation("D");
    }

    @Test
    void testGetAll() {
        List<RoomTypeModel> roomTypeList = new ArrayList<>();
        roomTypeList.add(roomTypeModel);

        when(roomTypeService.getAll()).thenReturn(roomTypeList);

        ResponseEntity<List<RoomTypeModel>> response = roomTypeController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Deluxe", response.getBody().get(0).getName());
    }

    @Test
    void testSave() throws Exception {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO("Deluxe", 2, "D", UUID.randomUUID());

        when(roomTypeService.save(roomTypeDTO)).thenReturn(roomTypeModel);

        ResponseEntity<RoomTypeModel> response = roomTypeController.save(roomTypeDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Deluxe", response.getBody().getName());
    }
}
