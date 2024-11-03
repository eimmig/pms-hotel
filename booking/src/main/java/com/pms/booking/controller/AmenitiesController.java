package com.pms.booking.controller;

import com.pms.booking.dto.*;
import com.pms.booking.model.AmenitiesModel;
import com.pms.booking.service.AmenitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/amenities")
public class AmenitiesController extends GenericController<AmenitiesModel, UUID, AmenitiesRequestDTO> {

    private final AmenitiesService amenitiesService;

    @Autowired
    public AmenitiesController(AmenitiesService amenitiesService) {
        super(amenitiesService);
        this.amenitiesService = amenitiesService;
    }

    @GetMapping("/getByIdWithRate/{id}")
    public ResponseEntity<AmenitiesRequestDTO> getByIdWithRate(@PathVariable UUID id) {
        Optional<AmenitiesRequestDTO> amenities = amenitiesService.getByIdWithRate(id);

        return amenities.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/getAllWithValue")
    public ResponseEntity<List<AmenitiesResponseDTO>> getAllWithValue() {

        List<AmenitiesResponseDTO> rate = amenitiesService.getAllWithValue();

        if (rate != null) {
            return ResponseEntity.ok(rate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/toBooking")
    public ResponseEntity<List<AmenitiesDTO>> getAllBooking() {

        List<AmenitiesDTO> list = amenitiesService.getAllBooking();

        if (list != null) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

