package com.pms.booking.controller;

import com.pms.booking.dto.*;
import com.pms.booking.model.BookingModel;
import com.pms.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingController extends GenericController<BookingModel, UUID, BookingDTO> {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        super(bookingService);
        this.bookingService = bookingService;
    }

    @PostMapping("/cancelBooking")
    public ResponseEntity<String> confirmBooking(@RequestBody @Valid BookingCancelDTO bookingCancelDTO) {
        try {
            bookingService.cancelBooking(bookingCancelDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Reserva quarto cancelado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cancelar reserva quarto: " + e.getMessage());
        }
    }

    @GetMapping("/roomtypechart")
    public InterativeChart getRoomTypeData() {
        return bookingService.getRoomTypeData();
    }

    @GetMapping("/occupancychart")
    public InterativeChart getOccupancyData() {
        return bookingService.getOccupancyData();
    }

    @GetMapping("/revenuechart")
    public StaticChart getRevenueData() {
        return bookingService.getRevenueData();
    }

    @GetMapping("/checkinschart")
    public StaticChart getCheckinData() {
        return bookingService.getCheckinData();
    }

    @GetMapping("/checkoutschart")
    public StaticChart getCheckoutData() {
        return bookingService.getCheckoutData();
    }

    @GetMapping("/getAllBooking")
    public List<BookingReceive> getAllBooking() {
        return bookingService.getAllBooking();
    }

    @GetMapping("/getAllBookingCheckin")
    public List<BookingReceive> getAllBookingCheckin() {
        return bookingService.getAllBookingByStatus("P", "start_date");
    }

    @GetMapping("/getAllBookingCheckout")
    public List<BookingReceive> getAllBookingCheckout() {
        return bookingService.getAllBookingByStatus("E", "end_date");
    }

    @PostMapping("/checkin/{id}")
    public ResponseEntity<String> checkinBooking(@PathVariable UUID id) {
        boolean success = bookingService.checkin(id);
        if (success) {
            return ResponseEntity.ok("Check-in realizado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar check-in");
        }
    }

    @PostMapping("/checkout/{id}")
    public ResponseEntity<String> checkoutBooking(@PathVariable UUID id) {
        boolean success = bookingService.checkout(id);
        if (success) {
            return ResponseEntity.ok("Check-in realizado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar check-in");
        }
    }
}

