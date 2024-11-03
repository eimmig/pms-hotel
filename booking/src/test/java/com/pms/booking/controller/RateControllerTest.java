package com.pms.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.booking.dto.RateRequestDTO;
import com.pms.booking.dto.RateResponseDTO;
import com.pms.booking.model.RateModel;
import com.pms.booking.service.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RateService rateService;

    @InjectMocks
    private RateController rateController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(rateController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testSave() throws Exception {
        RateRequestDTO rateRequestDTO = new RateRequestDTO(
                null,
                "Test Rate",
                UUID.randomUUID(),
                true,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100)
        );

        RateModel savedRateModel = new RateModel(UUID.randomUUID(), "Test Rate");

        when(rateService.save(any(RateRequestDTO.class))).thenReturn(savedRateModel);

        mockMvc.perform(post("/api/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rateRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Rate"));

        verify(rateService, times(1)).save(any(RateRequestDTO.class));
    }

    @Test
    void testGetAllWithValue() throws Exception {
        RateResponseDTO rateResponseDTO = new RateResponseDTO() {
            @Override
            public UUID getId() {
                return UUID.randomUUID();
            }

            @Override
            public String getName() {
                return "Test Rate";
            }

            @Override
            public UUID getRoomRateId() {
                return UUID.randomUUID();
            }

            @Override
            public boolean isGarageIncluded() {
                return true;
            }

            @Override
            public double getMondayRate() {
                return 100.0;
            }

            @Override
            public double getTuesdayRate() {
                return 100.0;
            }

            @Override
            public double getWednesdayRate() {
                return 100.0;
            }

            @Override
            public double getThursdayRate() {
                return 100.0;
            }

            @Override
            public double getFridayRate() {
                return 100.0;
            }

            @Override
            public double getSaturdayRate() {
                return 100.0;
            }

            @Override
            public double getSundayRate() {
                return 100.0;
            }
        };

        List<RateResponseDTO> rates = Collections.singletonList(rateResponseDTO);

        when(rateService.getAllWithValue()).thenReturn(rates);

        mockMvc.perform(get("/api/rate/getAllWithValue")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Rate"));

        verify(rateService, times(1)).getAllWithValue();
    }

    @Test
    void testGetByIdWithRate() throws Exception {
        UUID rateId = UUID.randomUUID();
        RateRequestDTO rateRequestDTO = new RateRequestDTO(
                rateId,
                "Test Rate",
                UUID.randomUUID(),
                true,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100)
        );

        when(rateService.getByIdWithRate(rateId)).thenReturn(Optional.of(rateRequestDTO));

        mockMvc.perform(get("/api/rate/getByIdWithRate/{id}", rateId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Rate"));

        verify(rateService, times(1)).getByIdWithRate(rateId);
    }

    @Test
    void testGetByIdWithRate_NotFound() throws Exception {
        UUID rateId = UUID.randomUUID();
        when(rateService.getByIdWithRate(rateId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rate/getByIdWithRate/{id}", rateId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(rateService, times(1)).getByIdWithRate(rateId);
    }
}
