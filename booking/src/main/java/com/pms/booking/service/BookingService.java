package com.pms.booking.service;

import com.pms.booking.dto.*;
import com.pms.booking.enums.EBookingRoomStatus;
import com.pms.booking.enums.EBookingStatus;
import com.pms.booking.enums.ERoomStatus;
import com.pms.booking.exception.BusinessException;
import com.pms.booking.model.*;
import com.pms.booking.producers.BookingEmailProducer;
import com.pms.booking.repository.*;
import com.pms.booking.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService extends GenericService<BookingModel, UUID, BookingDTO> {

    private final BookingRepository bookingRepository;
    private final BookingEmailProducer bookingEmailProducer;
    private final BookingRoomService bookingRoomService;
    private final PersonRepository personRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final RoomRepository roomRepository;

    private final BookingRoomAmenitiesRepository bookingRoomAmenitiesRepository;


    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          BookingEmailProducer bookingEmailProducer,
                          BookingRoomService bookingRoomService, PersonRepository personRepository, BookingRoomRepository bookingRoomRepository, RoomRepository roomRepository, BookingRoomAmenitiesRepository bookingRoomAmenitiesRepository) {
        super(bookingRepository);
        this.bookingRepository = bookingRepository;
        this.bookingEmailProducer = bookingEmailProducer;
        this.bookingRoomService = bookingRoomService;
        this.personRepository = personRepository;
        this.bookingRoomRepository = bookingRoomRepository;
        this.roomRepository = roomRepository;
        this.bookingRoomAmenitiesRepository = bookingRoomAmenitiesRepository;
    }

    @Override
    protected BookingModel convertToModel(BookingDTO bookingDTO, UUID id) {
        EBookingStatus eBookingStatus = EBookingStatus.fromCode(bookingDTO.status());

        var bookingModel = new BookingModel();

        Optional<PersonModel> personModelOpc = personRepository.findById(bookingDTO.personId());

        if (personModelOpc.isPresent()) {
            BeanUtils.copyProperties(bookingDTO, bookingModel);
            bookingModel.setPerson(personModelOpc.get());
            bookingModel.setStatus(eBookingStatus.getCode());
            bookingModel.setId(id);
            return bookingModel;
        } else {
            throw  new BusinessException("Pessoa não encontrada para o cadastro.");
        }
    }

    @Override
    @Transactional
    public BookingModel save(BookingDTO bookingDTO) throws Exception {
        BookingModel model = convertToModel(bookingDTO, null);
        BookingModel savedModel = repository.save(model);

        try {
            for (RoomListDTO roomListDTO : bookingDTO.roomList()) {
                BookingRoomDTO bookingRoomDTO = new BookingRoomDTO(
                        savedModel.getId(),
                        roomListDTO.roomId(),
                        savedModel.getStatus(),
                        savedModel.getStartDate(),
                        savedModel.getEndDate(),
                        roomListDTO.amenities());
                bookingRoomService.save(bookingRoomDTO);
            }

            BookingEmailDTO bookingEmailDTO = new BookingEmailDTO(savedModel.getId(), savedModel.getPerson().getEmail(), "Confirmação de Reserva", "Obrigado por se hospedar conosco. Sua reserva está CONFIRMADA!");

            bookingEmailProducer.publishMessageBookingRoom(bookingEmailDTO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao vincular reserva." + e, e);
        }
        return savedModel;
    }

    @Override
    @Transactional
    public boolean delete(UUID id) throws RuntimeException{
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw  new RuntimeException("Item não encontrado");
    }

    @Override
    @Transactional
    public BookingModel update(UUID id, BookingDTO bookingDTO) throws RuntimeException {
        if (repository.existsById(id)) {
            BookingModel model = convertToModel(bookingDTO, id);
            try {
                bookingRoomService.update(bookingDTO, model);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao vincular reserva. Compensação aplicada.", e);
            }
            return repository.save(model);
        }
        throw new RuntimeException("Item não encontrado");
    }

    public void cancelBooking(BookingCancelDTO bookingCancelDTO) throws Exception {
        Optional<BookingModel> bookingModelOpt = bookingRepository.findById(bookingCancelDTO.bookingId());

        if (bookingModelOpt.isPresent()) {
            BookingModel bookingModel = bookingModelOpt.get();

            if (bookingModel.getStatus().equals(EBookingStatus.EXECUTED.getCode())) {
                throw new BusinessException("Não é possivel cancelar reservas já efetivadas.");
            }

            bookingModel.setStatus(EBookingStatus.CANCELLED.getCode());
            bookingRepository.save(bookingModel);

            try {
               bookingRoomService.cancelBooking(bookingCancelDTO);
            } catch (Exception e) {
                bookingModel.setStatus(EBookingStatus.PENDING.getCode());
                bookingRepository.save(bookingModel);
                throw new RuntimeException("Erro ao cancelar a reserva. Rollback realizado.", e);
            }
        } else {
            throw new Exception("Reserva não encontrada para cancelamento.");
        }
    }

    public InterativeChart getRoomTypeData() {
        return bookingRoomService.getRoomTypeData();
    }

    public InterativeChart getOccupancyData() {
        return bookingRoomService.getOccupancyData();
    }

    public StaticChart getRevenueData() {
        return bookingRoomService.getRevenueToday();
    }

    public StaticChart getCheckinData() {
        return bookingRoomService.getCheckinsToday();
    }

    public StaticChart getCheckoutData() {
        return bookingRoomService.getCheckoutsToday();
    }

    public List<BookingReceive> getAllBooking() {
        List<BookingModel> listBm = repository.findAll();

        List<BookingReceive> list = new ArrayList<>();


        for (BookingModel bm : listBm) {
            List<RoomReceive> roomList = new ArrayList<>();
            List<BookingRoomModel> listBrm = bookingRoomService.getAllByBooking(bm);
            for (BookingRoomModel brm : listBrm) {
                List<BookingRoomAmenitiesModel> listAmenities = bookingRoomAmenitiesRepository.findByBookingRoomId(brm.getId());
                List<AmenitiesDTO> listAmeniti = new ArrayList<>();
                for (BookingRoomAmenitiesModel bra : listAmenities) {
                    listAmeniti.add(new AmenitiesDTO(bra.getAmenities().getId(), bra.getAmenities().getName()));
                }
                roomList.add(new RoomReceive(brm.getRoom().getId(), brm.getRoom().getNumber(), listAmeniti));
            }
            list.add(new BookingReceive(
                    bm.getId(),
                    DateUtils.formatDate(bm.getStartDate()),
                    DateUtils.formatDate(bm.getEndDate()),
                    bm.getPerson().getId(),
                    bm.getPerson().getName(),
                    bm.getStatus(),
                   convertStatus(EBookingStatus.fromCode(bm.getStatus()).name()),
                    roomList
            ));
        }
        return list;
    }

    public List<BookingReceive> getAllBookingByStatus(String status, String variable) {
        List<BookingModel> listBm = variable.equals("start_date")
                ? bookingRepository.findByStatusStartDate(status)
                : bookingRepository.findByStatusEndDate(status);

        return listBm.stream()
                .map(this::convertBookingModelToReceive)
                .collect(Collectors.toList());
    }

    private BookingReceive convertBookingModelToReceive(BookingModel bm) {
        List<RoomReceive> roomList = bookingRoomService.getAllByBooking(bm).stream()
                .map(this::convertBookingRoomModelToReceive)
                .collect(Collectors.toList());

        return new BookingReceive(
                bm.getId(),
                DateUtils.formatDate(bm.getStartDate()),
                DateUtils.formatDate(bm.getEndDate()),
                bm.getPerson().getId(),
                bm.getPerson().getName(),
                bm.getStatus(),
                convertStatus(EBookingStatus.fromCode(bm.getStatus()).name()),
                roomList
        );
    }

    private RoomReceive convertBookingRoomModelToReceive(BookingRoomModel brm) {
        List<AmenitiesDTO> amenities = bookingRoomAmenitiesRepository.findByBookingRoomId(brm.getId()).stream()
                .map(bra -> new AmenitiesDTO(bra.getAmenities().getId(), bra.getAmenities().getName()))
                .collect(Collectors.toList());

        return new RoomReceive(brm.getRoom().getId(), brm.getRoom().getNumber(), amenities);
    }

    @Transactional
    public Boolean checkin(UUID id) {
        try {
            BookingModel bookingModel = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

            List<BookingRoomModel> bookingRoomModels = bookingRoomService.getAllByBooking(bookingModel);

            for (BookingRoomModel bookingRoomModel : bookingRoomModels) {
                bookingRoomModel.setStatus(EBookingRoomStatus.EXECUTED.getCode());
                bookingRoomModel.getRoom().setStatus(ERoomStatus.OCCUPIED.getCode());

                bookingRoomRepository.save(bookingRoomModel);
                roomRepository.save(bookingRoomModel.getRoom());
            }

            bookingModel.setStatus(EBookingStatus.EXECUTED.getCode());
            bookingRepository.save(bookingModel);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Boolean checkout(UUID id) {
        try {
            BookingModel bookingModel = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

            List<BookingRoomModel> bookingRoomModels = bookingRoomService.getAllByBooking(bookingModel);

            for (BookingRoomModel bookingRoomModel : bookingRoomModels) {
                bookingRoomModel.setStatus(EBookingRoomStatus.EXECUTED.getCode());
                bookingRoomModel.getRoom().setStatus(ERoomStatus.AVAILABLE.getCode());

                bookingRoomRepository.save(bookingRoomModel);
                roomRepository.save(bookingRoomModel.getRoom());
            }

            bookingModel.setStatus(EBookingStatus.FINISHED.getCode());
            bookingRepository.save(bookingModel);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String convertStatus(String status) {
        return switch (status.toLowerCase()) {
            case "executed" -> "EFETIVADA";
            case "cancelled" -> "CANCELADA";
            case "pending" -> "PENDENTE";
            case "finished" -> "FINALIZADA";
            default -> throw new IllegalArgumentException("Status desconhecido: " + status);
        };
    }
}