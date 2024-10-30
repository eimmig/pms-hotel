package com.pms.booking.service;

import com.pms.booking.dto.*;
import com.pms.booking.model.*;
import com.pms.booking.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RateService extends GenericService<RateModel, UUID, RateRequestDTO> {

    private final RateRepository rateRepository;
    private final RoomRateRepository roomRateRepository;
    private final BookingRoomAmenitiesRepository bookingRoomAmenitiesRepository;
    private final RoomRateService roomRateService;
    private final AmenitiesRateRepository amenitiesRateRepository;
    private final BookingRoomRepository bookingRoomRepository;

    @Autowired
    public RateService(RateRepository rateRepository,
                       RoomRateService roomRateService,
                       RoomRateRepository roomRateRepository,
                       BookingRoomAmenitiesRepository bookingRoomAmenitiesRepository,
                       AmenitiesRateRepository amenitiesRateRepository,
                       BookingRoomRepository bookingRoomRepository) {
        super(rateRepository);
        this.rateRepository = rateRepository;
        this.roomRateRepository = roomRateRepository;
        this.bookingRoomAmenitiesRepository = bookingRoomAmenitiesRepository;
        this.roomRateService = roomRateService;
        this.amenitiesRateRepository = amenitiesRateRepository;
        this.bookingRoomRepository = bookingRoomRepository;
    }

    @Override
    protected RateModel convertToModel(RateRequestDTO rateRequestDTO, UUID id) {
        var rateModel = new RateModel();
        BeanUtils.copyProperties(rateRequestDTO, rateModel);
        rateModel.setId(id);
        return rateModel;
    }

    protected RateDTO convertToDTO(RateModel rateModel) {
        return new RateDTO(
                rateModel.getId(),
                rateModel.getName()
        );
    }

    @Transactional
    @Override
    public RateModel save(RateRequestDTO rateRequestDTO) throws Exception {
        RateModel rateModel = convertToModel(rateRequestDTO, null);

        RateModel savedRate = rateRepository.save(rateModel);

        RoomRateDTO dto = getRoomRateDTO(rateRequestDTO, rateModel);

        RoomRateModel roomRateModel = roomRateService.convertToModel(dto, null);
        roomRateModel.setRate(savedRate);

        roomRateService.saveFromModel(roomRateModel);

        return savedRate;
    }

    @Override
    @Transactional
    public boolean delete(UUID id) throws RuntimeException {
        //TODO VALIDAR SE A TARIFA NAO ESTA SENDO USADA EM NENHUMA CATEGORIA DE QUARTO
        if (rateRepository.existsById(id)) {
            RoomRateModel roomRateModel = roomRateRepository.findByRateId(id)
                    .orElse(null);

            if (roomRateModel != null) {
                roomRateRepository.delete(roomRateModel);
            }

            rateRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Item não encontrado");
    }


    @Override
    @Transactional
    public RateModel update(UUID id, RateRequestDTO rateRequestDTO) throws RuntimeException {
        if (rateRepository.existsById(id)) {
            RateModel rateModel = convertToModel(rateRequestDTO, id);

            RateModel updatedRates = rateRepository.save(rateModel);

            RoomRateModel existingRateModel = roomRateRepository.findByRate(updatedRates)
                    .orElseThrow(() -> new RuntimeException("Tarifas diárias não encontradas"));

            RoomRateDTO dto = getRoomRateDTO(rateRequestDTO, rateModel);

            RoomRateModel roomRateModel = roomRateService.convertToModel(dto, existingRateModel.getId());
            roomRateModel.setRate(updatedRates);

            roomRateRepository.save(roomRateModel);

            return updatedRates;
        }
        throw new RuntimeException("Item não encontrado");
    }

    public Optional<RateRequestDTO> getByIdWithRate(UUID id) {
        Optional<RateModel> rate = rateRepository.findById(id);

        if (rate.isPresent()) {
            Optional<RoomRateModel> roomRateModel = roomRateRepository.findByRate(rate.get());
            if (roomRateModel.isPresent()) {
                return Optional.of(new RateRequestDTO(
                        rate.get().getId(),
                        rate.get().getName(),
                        roomRateModel.get().getId(),
                        roomRateModel.get().getGarageIncluded(),
                        roomRateModel.get().getMondayRate(),
                        roomRateModel.get().getTuesdayRate(),
                        roomRateModel.get().getWednesdayRate(),
                        roomRateModel.get().getThursdayRate(),
                        roomRateModel.get().getFridayRate(),
                        roomRateModel.get().getSaturdayRate(),
                        roomRateModel.get().getSundayRate())
                );
            }
        }
        throw new RuntimeException("Tarifa não encontrada");
    }


    public TotalValueDTO getTotalValueByRoom(UUID bookingRoomId, LocalDate startDate, LocalDate endDate, UUID rateId) {
        Optional<RoomRateModel> rateOptional = roomRateRepository.findByRateId(rateId);
        List<BookingRoomAmenitiesModel> bookingRoomAmenitiesModel = bookingRoomAmenitiesRepository.findByBookingRoomId(bookingRoomId);

        if (rateOptional.isEmpty()) {
            throw new RuntimeException("Tarifa não encontrada para a categoria");
        }

        RoomRateModel rateModel = rateOptional.get();

        BigDecimal bookinglValue = BigDecimal.ZERO;
        BigDecimal amenitiesValue = BigDecimal.ZERO;
        LocalDate currentDate = startDate;

        while (currentDate.isBefore(endDate)) {
            bookinglValue = bookinglValue.add(getRateForDay(rateModel, currentDate));
            currentDate = currentDate.plusDays(1);
        }

        if (!bookingRoomAmenitiesModel.isEmpty()) {
            currentDate = startDate;
            for (BookingRoomAmenitiesModel bra : bookingRoomAmenitiesModel) {

                Optional<AmenitiesRateModel> amenitiesRateModel = amenitiesRateRepository.findByAmenities(bra.getAmenities());

                if (amenitiesRateModel.isPresent()) {
                    AmenitiesRateModel amenities = amenitiesRateModel.get();
                    while (currentDate.isBefore(endDate)) {
                        bookinglValue = bookinglValue.add(getAmenitiesRateForDay(amenities, currentDate));
                        currentDate = currentDate.plusDays(1);
                    }
                }
            }
        }


        return new TotalValueDTO(bookinglValue, amenitiesValue);
    }


    public TotalValueDTO getTotalValueByBooking(UUID bookingId) {
        BigDecimal bookingValue = BigDecimal.ZERO;
        BigDecimal amenitiesValue = BigDecimal.ZERO;

        List<BookingRoomModel> bookingRoomModels = bookingRoomRepository.findAllByBookingId(bookingId);
        for (BookingRoomModel bookingRoomModel : bookingRoomModels) {
            UUID rateId = bookingRoomModel.getRoom().getRoomType().getRate().getId();

            LocalDate startLocalDate = bookingRoomModel.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate endLocalDate = bookingRoomModel.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Optional<RoomRateModel> rateOptional = roomRateRepository.findByRateId(rateId);
            List<BookingRoomAmenitiesModel> bookingRoomAmenitiesModel = bookingRoomAmenitiesRepository.findByBookingRoomId(bookingRoomModel.getId());

            if (rateOptional.isEmpty()) {
                throw new RuntimeException("Tarifa não encontrada para a categoria");
            }

            RoomRateModel rateModel = rateOptional.get();

            LocalDate currentDate = startLocalDate;

            while (currentDate.isBefore(endLocalDate) || currentDate.isEqual(endLocalDate)) {
                if (currentDate.isEqual(endLocalDate) && isAfterNoon()) {
                    bookingValue = bookingValue.add(getRateForDay(rateModel, currentDate).multiply(BigDecimal.valueOf(1.5)));
                } else {
                    bookingValue = bookingValue.add(getRateForDay(rateModel, currentDate));
                }
                currentDate = currentDate.plusDays(1);
            }

            if (!bookingRoomAmenitiesModel.isEmpty()) {
                currentDate = startLocalDate;
                for (BookingRoomAmenitiesModel bra : bookingRoomAmenitiesModel) {
                    Optional<AmenitiesRateModel> amenitiesRateModel = amenitiesRateRepository.findByAmenities(bra.getAmenities());

                    if (amenitiesRateModel.isPresent()) {
                        AmenitiesRateModel amenities = amenitiesRateModel.get();
                        currentDate = startLocalDate;

                        while (currentDate.isBefore(endLocalDate) || currentDate.isEqual(endLocalDate)) {
                            amenitiesValue = amenitiesValue.add(getAmenitiesRateForDay(amenities, currentDate));
                            currentDate = currentDate.plusDays(1);
                        }
                    }
                }
            }
        }

        return new TotalValueDTO(bookingValue, amenitiesValue);
    }

    private boolean isAfterNoon() {
        return LocalTime.now().isAfter(LocalTime.NOON);
    }

    public List<RateResponseDTO> getAllWithValue() {
        return rateRepository.findAllWithValue();
    }

    private BigDecimal getRateForDay(RoomRateModel rateModel, LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> rateModel.getMondayRate();
            case TUESDAY -> rateModel.getTuesdayRate();
            case WEDNESDAY -> rateModel.getWednesdayRate();
            case THURSDAY -> rateModel.getThursdayRate();
            case FRIDAY -> rateModel.getFridayRate();
            case SATURDAY -> rateModel.getSaturdayRate();
            case SUNDAY -> rateModel.getSundayRate();
        };
    }

    private BigDecimal getAmenitiesRateForDay(AmenitiesRateModel rateModel, LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> rateModel.getMondayRate();
            case TUESDAY -> rateModel.getTuesdayRate();
            case WEDNESDAY -> rateModel.getWednesdayRate();
            case THURSDAY -> rateModel.getThursdayRate();
            case FRIDAY -> rateModel.getFridayRate();
            case SATURDAY -> rateModel.getSaturdayRate();
            case SUNDAY -> rateModel.getSundayRate();
        };
    }

    private RoomRateDTO getRoomRateDTO(RateRequestDTO rateRequestDTO, RateModel rateModel) {
        return new RoomRateDTO(
                null,
                rateModel.getId(),
                rateRequestDTO.mondayRate(),
                rateRequestDTO.tuesdayRate(),
                rateRequestDTO.wednesdayRate(),
                rateRequestDTO.thursdayRate(),
                rateRequestDTO.fridayRate(),
                rateRequestDTO.saturdayRate(),
                rateRequestDTO.sundayRate(),
                rateRequestDTO.garageIncluded());
    }
}