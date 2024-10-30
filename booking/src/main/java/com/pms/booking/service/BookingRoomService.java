package com.pms.booking.service;

import com.pms.booking.dto.*;
import com.pms.booking.enums.EBookingRoomStatus;
import com.pms.booking.enums.ERoomStatus;
import com.pms.booking.exception.BusinessException;
import com.pms.booking.model.BookingModel;
import com.pms.booking.model.BookingRoomModel;
import com.pms.booking.model.RoomModel;
import com.pms.booking.repository.BookingRepository;
import com.pms.booking.repository.BookingRoomRepository;
import com.pms.booking.repository.RoomRepository;
import com.pms.booking.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingRoomService extends GenericService<BookingRoomModel, UUID, BookingRoomDTO> {

    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final BookingRepository bookingRepository;
    private final BookingRoomAmenitiesService bookingRoomAmenitiesService;
    private final RateService rateService;

    @Autowired
    public BookingRoomService(BookingRoomRepository bookingRoomRepository, RoomRepository roomRepository, BookingRepository bookingRepository, BookingRoomAmenitiesService bookingRoomAmenitiesService, RateService rateService) {
        super(bookingRoomRepository);
        this.roomRepository = roomRepository;
        this.bookingRoomRepository = bookingRoomRepository;
        this.bookingRepository = bookingRepository;
        this.bookingRoomAmenitiesService = bookingRoomAmenitiesService;
        this.rateService = rateService;
    }

    @Override
    protected BookingRoomModel convertToModel(BookingRoomDTO bookingRoomDTO, UUID id) {
        var bookingRoomModel = new BookingRoomModel();
        BeanUtils.copyProperties(bookingRoomDTO, bookingRoomModel);

        Optional<RoomModel> roomModelOpc =  roomRepository.findById(bookingRoomDTO.roomId());
        Optional<BookingModel> bookingModelOpc =  bookingRepository.findById(bookingRoomDTO.bookingId());


        if (roomModelOpc.isPresent() && bookingModelOpc.isPresent()) {
            bookingRoomModel.setRoom(roomModelOpc.get());
            bookingRoomModel.setBooking(bookingModelOpc.get());
        } else {
            throw new RuntimeException("Erro ao converter dado!");
        }

        bookingRoomModel.setId(id);
        return bookingRoomModel;
    }

    @Override
    @Transactional
    public BookingRoomModel save(BookingRoomDTO bookingRoomDTO) throws Exception {
        try {
            BookingRoomModel bookingRoomModel = convertToModel(bookingRoomDTO, null);
            bookingRoomModel = repository.save(bookingRoomModel);

            if (!bookingRoomDTO.amenities().isEmpty()) {
                for (AmenitiesDTO amenitiesDTO : bookingRoomDTO.amenities()) {
                    BookingRoomAmenitiesDTO bookingRoomAmenitiesDTO = new BookingRoomAmenitiesDTO(null, bookingRoomModel.getId(), amenitiesDTO.amenitieId());
                    bookingRoomAmenitiesService.save(bookingRoomAmenitiesDTO);
                }
            }

            LocalDate startDate = LocalDate.parse(DateUtils.formatDate(bookingRoomModel.getStartDate()));
            LocalDate endDate = LocalDate.parse(DateUtils.formatDate(bookingRoomModel.getEndDate()));

            UUID rate = bookingRoomModel.getRoom().getRoomType().getRate().getId();

            TotalValueDTO totalValueDTO = rateService.getTotalValueByRoom(bookingRoomModel.getId(), startDate, endDate, rate);

            bookingRoomModel.setBookingValue(totalValueDTO.bookingRoom());
            bookingRoomModel.setAmenitiesValue(totalValueDTO.amenities());

            repository.save(bookingRoomModel);

            return bookingRoomModel;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar os quartos ou serviços adicionais.");
        }
    }

    public void confirmBooking(BookingRoomDTO bookingRoomDTO) throws BusinessException {
        Optional<BookingRoomModel> roomOpt = bookingRoomRepository.findByBookingIdAndRoomId(bookingRoomDTO.bookingId(), bookingRoomDTO.roomId());
        if (roomOpt.isEmpty()) {
            throw new BusinessException("Quarto não encontrado.");
        }

        BookingRoomModel bookingRoom = roomOpt.get();
        RoomModel room = bookingRoom.getRoom();

        if (EBookingRoomStatus.EXECUTED.getCode().equals(bookingRoom.getStatus())) {
            return;
        }

        if (!ERoomStatus.AVAILABLE.getCode().equals(room.getStatus())) {
            throw new BusinessException("Quarto nº " + room.getNumber() + " não disponível para Check-in!");
        }

        bookingRoom.setStatus(EBookingRoomStatus.EXECUTED.getCode());
        room.setStatus(ERoomStatus.OCCUPIED.getCode());
        bookingRoomRepository.save(bookingRoom);
        roomRepository.save(room);
    }

    public void cancelBooking(BookingCancelDTO bookingCancelDTO) throws BusinessException {
        UUID bookingId = bookingCancelDTO.bookingId();

        for (UUID room : bookingCancelDTO.roomList()) {
            BookingRoomModel bookingRoomModel = findBookingRoom(bookingId, room);
            bookingRoomModel.setStatus(EBookingRoomStatus.CANCELLED.getCode());
            bookingRoomRepository.save(bookingRoomModel);
        }
    }

    public BookingRoomModel findBookingRoom(UUID bookingId, UUID roomId) throws BusinessException {
        return bookingRoomRepository.findByBookingIdAndRoomId(bookingId, roomId)
                .orElseThrow(() -> new BusinessException("Reserva Quarto não encontrado"));
    }

    @Transactional
    public void update(BookingDTO bookingDTO, BookingModel bookingModel) throws Exception {
        try {
            //Recupera os registros salvos e apaga
            List<BookingRoomModel> bookingRoomModelList = bookingRoomRepository.findAllByBooking(bookingModel);

            for (BookingRoomModel model : bookingRoomModelList) {
                //Antes de apagar vou verificar se nao tem extra vinculado
                bookingRoomAmenitiesService.prepareToUpdate(model.getId());
            }
            bookingRoomRepository.deleteAll(bookingRoomModelList);

            for (RoomListDTO roomListDTO : bookingDTO.roomList()) {
                BookingRoomDTO bookingRoomDTO = new BookingRoomDTO(
                        bookingModel.getId(),
                        roomListDTO.roomId(),
                        bookingModel.getStatus(),
                        bookingModel.getStartDate(),
                        bookingModel.getEndDate(),
                        roomListDTO.amenities());

                save(bookingRoomDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar os quartos ou serviços adicionais.");
        }
    }


    public InterativeChart getRoomTypeData() {
        List<Object[]> results = bookingRoomRepository.countBookingsByRoomType();

        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Object[] result : results) {
            Long count = (Long) result[0];
            String roomType = (String) result[1];

            labels.add(roomType);
            data.add(count.doubleValue());
        }

        return new InterativeChart(
                labels,
                "Tipo de Quarto",
                data
        );
    }

    public InterativeChart getOccupancyData() {
        List<Object[]> results = bookingRoomRepository.countBookingsForOccupation();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Object[] result : results) {
            labels.add(result[0].toString());
            data.add(((Number) result[1]).doubleValue());
        }

        return new InterativeChart(labels, "Ocupação", data);
    }

    public StaticChart getCheckinsToday() {
        return new StaticChart(bookingRoomRepository.countCheckinsToday());
    }

    public StaticChart getRevenueToday() {
        return new StaticChart(bookingRoomRepository.getRevenueToday());
    }

    public StaticChart getCheckoutsToday() {
        return new StaticChart(bookingRoomRepository.countCheckoutsToday());

    }

    public List<BookingRoomModel> getAllByBooking(BookingModel bm) {
        return bookingRoomRepository.findAllByBooking(bm);
    }
}