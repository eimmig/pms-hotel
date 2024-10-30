package com.pms.booking.service;

import com.pms.booking.dto.RoomDTO;
import com.pms.booking.dto.RoomReciveListDTO;
import com.pms.booking.enums.ERoomStatus;
import com.pms.booking.exception.BusinessException;
import com.pms.booking.model.RoomModel;
import com.pms.booking.model.RoomTypeModel;
import com.pms.booking.repository.RoomRepository;
import com.pms.booking.repository.RoomTypeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomService extends GenericService<RoomModel, UUID, RoomDTO> {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        super(roomRepository);
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    protected RoomModel convertToModel(RoomDTO roomDTO, UUID id) {
        ERoomStatus eRoomStatus = ERoomStatus.fromCode(roomDTO.status());
        var roomModel = new RoomModel();
        BeanUtils.copyProperties(roomDTO, roomModel);

        Optional<RoomTypeModel> roomTypeModelOpc = roomTypeRepository.findById(roomDTO.roomTypeId());

        if (roomTypeModelOpc.isPresent()) {
            roomModel.setRoomType(roomTypeModelOpc.get());
            roomModel.setStatus(eRoomStatus.getCode());
            roomModel.setId(id);
            return roomModel;
        } else {
            throw new BusinessException("Categoria não encontrada para o cadastro");
        }
    }

    //TODO Ajustar para que seja considerado também o período de reservas, dessa maneira é possivel trabalhar com locação para hospede sem reserva.
    public boolean checkAvailability(UUID roomId) {
        return repository.findById(roomId)
                .map(room -> ERoomStatus.AVAILABLE.getCode().equals(room.getStatus()))
                .orElse(false);
    }

    public List<RoomReciveListDTO> getAllBooking(LocalDate startDate, LocalDate endDate) {
        List<RoomModel> roomModels = roomRepository.findAllWithDate(startDate, endDate);

        return roomModels.stream()
                .map(room -> new RoomReciveListDTO(room.getId(), room.getNumber()))
                .collect(Collectors.toList());
    }
}