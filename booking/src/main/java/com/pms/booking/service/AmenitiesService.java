package com.pms.booking.service;

import com.pms.booking.dto.*;
import com.pms.booking.model.AmenitiesModel;
import com.pms.booking.model.AmenitiesRateModel;
import com.pms.booking.model.RateModel;
import com.pms.booking.repository.AmenitiesRateRepository;
import com.pms.booking.repository.AmenitiesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AmenitiesService extends GenericService<AmenitiesModel, UUID, AmenitiesRequestDTO> {

    private final AmenitiesRepository amenitiesRepository;
    private final AmenitiesRateRepository amenitiesRateRepository;

    private final AmenitiesRateService amenitiesRateService;

    @Autowired
    public AmenitiesService(AmenitiesRepository amenitiesRepository, AmenitiesRateRepository amenitiesRateRepository, AmenitiesRateService amenitiesRateService) {
        super(amenitiesRepository);
        this.amenitiesRateRepository = amenitiesRateRepository;
        this.amenitiesRepository = amenitiesRepository;
        this.amenitiesRateService = amenitiesRateService;
    }

    @Override
    protected AmenitiesModel convertToModel(AmenitiesRequestDTO amenitiesRequestDTO, UUID id) {
        var amenitiesModel = new AmenitiesModel();
        BeanUtils.copyProperties(amenitiesRequestDTO, amenitiesModel);
        amenitiesModel.setId(id);
        return amenitiesModel;
    }

    protected AmenitiesDTO convertToDTO(AmenitiesModel amenitiesModel) {
        return new AmenitiesDTO(
                amenitiesModel.getId(),
                amenitiesModel.getName()
        );
    }


    @Transactional
    @Override
    public AmenitiesModel save(AmenitiesRequestDTO amenitiesRequestDTO) throws Exception {
        AmenitiesModel amenitiesModel = convertToModel(amenitiesRequestDTO, null);

        AmenitiesModel savedAmenities = amenitiesRepository.save(amenitiesModel);

        AmenitiesRateDTO dto = getRoomRateDTO(amenitiesRequestDTO, savedAmenities);

        AmenitiesRateModel amenitiesRateModel = amenitiesRateService.convertToModel(dto, null);
        amenitiesRateModel.setAmenities(savedAmenities);

        amenitiesRateService.saveFromModel(amenitiesRateModel);

        return savedAmenities;
    }

    @Override
    @Transactional
    public boolean delete(UUID id) throws RuntimeException {
        if (amenitiesRepository.existsById(id)) {
            AmenitiesRateModel amenitiesRate = amenitiesRateRepository.findByAmenitiesId(id)
                    .orElse(null);

            if (amenitiesRate != null) {
                amenitiesRateRepository.delete(amenitiesRate);
            }

            amenitiesRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Item não encontrado");
    }


    @Override
    @Transactional
    public AmenitiesModel update(UUID id, AmenitiesRequestDTO amenitiesRequestDTO) throws RuntimeException {
        if (amenitiesRepository.existsById(id)) {
            AmenitiesModel amenitiesModel = convertToModel(amenitiesRequestDTO, id);

            AmenitiesModel updatedAmenities = amenitiesRepository.save(amenitiesModel);

            AmenitiesRateModel existingRateModel = amenitiesRateRepository.findByAmenities(updatedAmenities)
                    .orElseThrow(() -> new RuntimeException("Taxa de amenidade não encontrada"));

            AmenitiesRateDTO dto = getRoomRateDTO(amenitiesRequestDTO, updatedAmenities);


            AmenitiesRateModel amenitiesRateModel = amenitiesRateService.convertToModel(dto, existingRateModel.getId());
            amenitiesRateModel.setAmenities(updatedAmenities);

            amenitiesRateRepository.save(amenitiesRateModel);

            return updatedAmenities;
        }
        throw new RuntimeException("Item não encontrado");
    }

    public Optional<AmenitiesRequestDTO> getByIdWithRate(UUID id) {
        Optional<AmenitiesModel> amenities = amenitiesRepository.findById(id);

        if (amenities.isPresent()) {
            Optional<AmenitiesRateModel> amenitiesRateModel = amenitiesRateRepository.findByAmenities(amenities.get());
            if (amenitiesRateModel.isPresent()) {
                return Optional.of(new AmenitiesRequestDTO(
                        amenities.get().getId(),
                        amenities.get().getName(),
                        amenitiesRateModel.get().getId(),
                        amenitiesRateModel.get().getMondayRate(),
                        amenitiesRateModel.get().getTuesdayRate(),
                        amenitiesRateModel.get().getWednesdayRate(),
                        amenitiesRateModel.get().getThursdayRate(),
                        amenitiesRateModel.get().getFridayRate(),
                        amenitiesRateModel.get().getSaturdayRate(),
                        amenitiesRateModel.get().getSundayRate())
                );
            }
        }
        throw new RuntimeException("Tarifa não encontrada");
    }

    public List<AmenitiesResponseDTO> getAllWithValue() {
        return amenitiesRepository.findAllWithValue();
    }

    public List<AmenitieReciveDTO> getAllBooking() {
        List<AmenitiesModel> amenitiesModels = amenitiesRepository.findAll();

        return amenitiesModels.stream()
                .map(am -> new AmenitieReciveDTO(am.getId(), am.getName()))
                .collect(Collectors.toList());
    }


    private AmenitiesRateDTO getRoomRateDTO(AmenitiesRequestDTO rateRequestDTO, AmenitiesModel rateModel) {
        return new AmenitiesRateDTO(
                null,
                rateModel.getId(),
                rateRequestDTO.mondayRate(),
                rateRequestDTO.tuesdayRate(),
                rateRequestDTO.wednesdayRate(),
                rateRequestDTO.thursdayRate(),
                rateRequestDTO.fridayRate(),
                rateRequestDTO.saturdayRate(),
                rateRequestDTO.sundayRate());
    }
}