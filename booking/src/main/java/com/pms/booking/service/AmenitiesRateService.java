package com.pms.booking.service;

import com.pms.booking.dto.AmenitiesRateDTO;
import com.pms.booking.model.AmenitiesRateModel;
import com.pms.booking.repository.AmenitiesRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AmenitiesRateService extends GenericService<AmenitiesRateModel, UUID, AmenitiesRateDTO> {

    private final AmenitiesRateRepository amenitiesRateRepository;

    @Autowired
    public AmenitiesRateService(AmenitiesRateRepository amenitiesRateRepository) {
        super(amenitiesRateRepository);
        this.amenitiesRateRepository = amenitiesRateRepository;
    }

    @Override
    protected AmenitiesRateModel convertToModel(AmenitiesRateDTO amenitiesRateDTO, UUID id) {
        var amenitiesRateModel = new AmenitiesRateModel();
        BeanUtils.copyProperties(amenitiesRateDTO, amenitiesRateModel);
        amenitiesRateModel.setId(id);
        return amenitiesRateModel;
    }

    protected AmenitiesRateDTO convertToDTO(AmenitiesRateModel amenitiesRateModel) {
        return new AmenitiesRateDTO(
                amenitiesRateModel.getId(),
                amenitiesRateModel.getAmenities().getId(),
                amenitiesRateModel.getMondayRate(),
                amenitiesRateModel.getTuesdayRate(),
                amenitiesRateModel.getWednesdayRate(),
                amenitiesRateModel.getThursdayRate(),
                amenitiesRateModel.getFridayRate(),
                amenitiesRateModel.getSaturdayRate(),
                amenitiesRateModel.getSundayRate()

        );
    }


    @Transactional
    public AmenitiesRateModel saveFromModel(AmenitiesRateModel amenitiesModel) throws Exception {
        return amenitiesRateRepository.save(amenitiesModel);
    }
}