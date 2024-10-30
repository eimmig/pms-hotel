package com.pms.booking.service;

import com.pms.booking.dto.AmenitiesDTO;
import com.pms.booking.dto.AmenitiesRateDTO;
import com.pms.booking.dto.AmenitiesRequestDTO;
import com.pms.booking.dto.BookingRoomAmenitiesDTO;
import com.pms.booking.model.AmenitiesModel;
import com.pms.booking.model.AmenitiesRateModel;
import com.pms.booking.model.BookingRoomAmenitiesModel;
import com.pms.booking.model.BookingRoomModel;
import com.pms.booking.repository.AmenitiesRateRepository;
import com.pms.booking.repository.AmenitiesRepository;
import com.pms.booking.repository.BookingRoomAmenitiesRepository;
import com.pms.booking.repository.BookingRoomRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingRoomAmenitiesService extends GenericService<BookingRoomAmenitiesModel, UUID, BookingRoomAmenitiesDTO> {

    private final BookingRoomAmenitiesRepository bookingRoomAmenitiesRepository;
    private final AmenitiesRateRepository amenitiesRateRepository;
    private final AmenitiesService amenitiesService;
    private final AmenitiesRateService amenitiesRateService;
    private final AmenitiesRepository amenitiesRepository;
    private final BookingRoomRepository bookingRoomRepository;

    @Autowired
    public BookingRoomAmenitiesService(BookingRoomAmenitiesRepository bookingRoomAmenitiesRepository,
                                       AmenitiesRateService amenitiesRateService,
                                       AmenitiesService amenitiesService,
                                       AmenitiesRateRepository amenitiesRateRepository,
                                       AmenitiesRepository amenitiesRepository, BookingRoomRepository bookingRoomRepository)
    {
        super(bookingRoomAmenitiesRepository);
        this.bookingRoomAmenitiesRepository = bookingRoomAmenitiesRepository;
        this.amenitiesRateService = amenitiesRateService;
        this.amenitiesService = amenitiesService;
        this.amenitiesRateRepository = amenitiesRateRepository;
        this.amenitiesRepository = amenitiesRepository;
        this.bookingRoomRepository = bookingRoomRepository;
    }

    @Override
    protected BookingRoomAmenitiesModel convertToModel(BookingRoomAmenitiesDTO bookingRoomAmenitiesDTO, UUID id) {
        var bookingRoomAmenitiesModel = new BookingRoomAmenitiesModel();

        Optional<AmenitiesModel> amenitiesModelOpc = amenitiesRepository.findById(bookingRoomAmenitiesDTO.amenities());
        Optional<BookingRoomModel> bookingRoomModelOpc = bookingRoomRepository.findById(bookingRoomAmenitiesDTO.bookingRate());

        if (amenitiesModelOpc.isPresent() && bookingRoomModelOpc.isPresent()) {
            bookingRoomAmenitiesModel.setAmenities(amenitiesModelOpc.get());
            bookingRoomAmenitiesModel.setBookingRoom(bookingRoomModelOpc.get());
            bookingRoomAmenitiesModel.setId(id);
            return bookingRoomAmenitiesModel;
        }

        throw new RuntimeException("Erro ao converter comodidade adicional.");
    }

    public List<AmenitiesRequestDTO> getByBookingRoomWithRate(UUID bookingRoomId) {
        List<BookingRoomAmenitiesModel> bookingRoomAmenitiesModels = bookingRoomAmenitiesRepository.findByBookingRoomId(bookingRoomId);

        List<AmenitiesRequestDTO> amenitiesRequestDTOList = new ArrayList<>();

        for (BookingRoomAmenitiesModel bookingRoomAmenities : bookingRoomAmenitiesModels) {
            AmenitiesModel amenities = bookingRoomAmenities.getAmenities();


            Optional<AmenitiesRateModel> amenitiesRateModel = amenitiesRateRepository.findByAmenities(amenities);

            if (amenitiesRateModel.isPresent()) {

                AmenitiesRequestDTO amenitiesRequestDTO = new AmenitiesRequestDTO(
                        amenities.getId(),
                        amenities.getName(),
                        amenitiesRateModel.get().getId(),
                        amenitiesRateModel.get().getMondayRate(),
                        amenitiesRateModel.get().getTuesdayRate(),
                        amenitiesRateModel.get().getWednesdayRate(),
                        amenitiesRateModel.get().getThursdayRate(),
                        amenitiesRateModel.get().getFridayRate(),
                        amenitiesRateModel.get().getSaturdayRate(),
                        amenitiesRateModel.get().getSundayRate()
                );
                amenitiesRequestDTOList.add(amenitiesRequestDTO);
            }
        }

        if (amenitiesRequestDTOList.isEmpty()) {
            throw new RuntimeException("Nenhuma tarifa encontrada para as comodidades.");
        }
        return amenitiesRequestDTOList;
    }

    @Override
    @Transactional
    public BookingRoomAmenitiesModel save(BookingRoomAmenitiesDTO dto) throws Exception {
        BookingRoomAmenitiesModel model = convertToModel(dto, null);

        List<BookingRoomAmenitiesModel> listModel = bookingRoomAmenitiesRepository.findByBookingRoomId(dto.bookingRate());

        if (!listModel.isEmpty()) {
            repository.deleteAll();
        }
        return repository.save(model);
    }

    @Transactional
    public void prepareToUpdate( UUID bookingRoom) throws Exception {

        List<BookingRoomAmenitiesModel> amenitiesSaved = bookingRoomAmenitiesRepository.findByBookingRoomId(bookingRoom);

        repository.deleteAll(amenitiesSaved);
    }
}