package com.pms.booking.controller;

import com.pms.booking.dto.RoomTypeDTO;
import com.pms.booking.model.RoomTypeModel;
import com.pms.booking.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController extends GenericController<RoomTypeModel, UUID, RoomTypeDTO> {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService, RoomTypeService roomTypeService1) {
        super(roomTypeService);
        this.roomTypeService = roomTypeService1;
    }

    @GetMapping("/getRate/{id}")
    public ResponseEntity<String> getRateByRoomType(@PathVariable UUID id) {
        String rate = roomTypeService.getRateByRoomType(id);
        return ResponseEntity.ok(rate);
    }
}
