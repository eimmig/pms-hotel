package com.pms.booking.controller;

import com.pms.booking.dto.AmenitiesRequestDTO;
import com.pms.booking.dto.BookingRoomAmenitiesDTO;
import com.pms.booking.model.BookingRoomAmenitiesModel;
import com.pms.booking.service.BookingRoomAmenitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking-room-amenities")
public class BookingRoomAmenitiesController extends GenericController<BookingRoomAmenitiesModel, UUID, BookingRoomAmenitiesDTO> {

    private final BookingRoomAmenitiesService bookingRoomAmenitiesService;

    @Autowired
    public BookingRoomAmenitiesController(BookingRoomAmenitiesService bookingRoomAmenitiesService) {
        super(bookingRoomAmenitiesService);
        this.bookingRoomAmenitiesService = bookingRoomAmenitiesService;
    }

    @GetMapping("/getByBookingRoomWithRate/{bookingRoom}")
    public ResponseEntity<List<AmenitiesRequestDTO>> getByBookingRoomWithRate(@PathVariable UUID bookingRoom) {
        List<AmenitiesRequestDTO> amenities = bookingRoomAmenitiesService.getByBookingRoomWithRate(bookingRoom);

        if (!amenities.isEmpty()) {
            return ResponseEntity.ok(amenities);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

