package com.pms.booking.service;

import com.pms.booking.dto.RoomRateDTO;
import com.pms.booking.model.RoomRateModel;
import com.pms.booking.repository.RoomRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomRateService extends GenericService<RoomRateModel, UUID, RoomRateDTO> {

    private final RoomRateRepository roomRateRepository;

    @Autowired
    public RoomRateService(RoomRateRepository roomRateRepository) {
        super(roomRateRepository);
        this.roomRateRepository = roomRateRepository;
    }

    @Override
    protected RoomRateModel convertToModel(RoomRateDTO roomRateDTO, UUID id) {
        var roomModel = new RoomRateModel();
        BeanUtils.copyProperties(roomRateDTO, roomModel);
        roomModel.setId(id);
        return roomModel;
    }

    protected RoomRateDTO convertToDTO(RoomRateModel roomRateModel) {
        return new RoomRateDTO(
                roomRateModel.getId(),
                roomRateModel.getRate().getId(),
                roomRateModel.getMondayRate(),
                roomRateModel.getTuesdayRate(),
                roomRateModel.getWednesdayRate(),
                roomRateModel.getThursdayRate(),
                roomRateModel.getFridayRate(),
                roomRateModel.getSaturdayRate(),
                roomRateModel.getSundayRate(),
                roomRateModel.getGarageIncluded()
        );
    }

    @Transactional
    public RoomRateModel saveFromModel(RoomRateModel roomRateModel) throws Exception {
        return roomRateRepository.save(roomRateModel);
    }
}