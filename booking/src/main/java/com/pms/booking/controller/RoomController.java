package com.pms.booking.controller;

import com.pms.booking.dto.AmenitiesDTO;
import com.pms.booking.dto.RoomDTO;
import com.pms.booking.dto.RoomReciveListDTO;
import com.pms.booking.model.RoomModel;
import com.pms.booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/room")
public class RoomController extends GenericController<RoomModel, UUID, RoomDTO> {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        super(roomService);
        this.roomService = roomService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomReciveListDTO>> getAvailableRooms(@RequestParam String startDate, @RequestParam String endDate, @RequestParam(required = false) UUID bookingId) {
        if (startDate == null || endDate == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<RoomReciveListDTO> availableRooms = roomService.getAllBooking(LocalDate.parse(startDate), LocalDate.parse(endDate), bookingId);
            return ResponseEntity.ok(availableRooms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}

