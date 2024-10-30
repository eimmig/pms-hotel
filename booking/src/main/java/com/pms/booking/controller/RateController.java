package com.pms.booking.controller;

import com.pms.booking.dto.RateRequestDTO;
import com.pms.booking.dto.RateResponseDTO;
import com.pms.booking.dto.TotalValueDTO;
import com.pms.booking.model.RateModel;
import com.pms.booking.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/rate")
public class RateController extends GenericController<RateModel, UUID, RateRequestDTO> {

    private final RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        super(rateService);
        this.rateService = rateService;
    }

    @GetMapping("/getByIdWithRate/{id}")
    public ResponseEntity<RateRequestDTO> getByIdWithRate(@PathVariable UUID id) {
        Optional<RateRequestDTO> rate = rateService.getByIdWithRate(id);

        if (rate.isPresent()) {
            return ResponseEntity.ok(rate.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/getTotalValueByRoom/{bookingRoomId}")
    public ResponseEntity<TotalValueDTO> getTotalValueByRoom(
            @PathVariable UUID bookingRoomId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("rateId") UUID rateId) {

        TotalValueDTO rate = rateService.getTotalValueByRoom(bookingRoomId, startDate, endDate, rateId);

        if (rate != null) {
            return ResponseEntity.ok(rate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getAllWithValue")
    public ResponseEntity<List<RateResponseDTO>> getAllWithValue() {

        List<RateResponseDTO> rate = rateService.getAllWithValue();

        if (rate != null) {
            return ResponseEntity.ok(rate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getTotalValueByBooking/{bookingId}")
    public ResponseEntity<TotalValueDTO> getTotalValueByBooking(
            @PathVariable UUID bookingId) {

        TotalValueDTO rate = rateService.getTotalValueByBooking(bookingId);

        if (rate != null) {
            return ResponseEntity.ok(rate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}

