package com.pms.booking.service;

import com.pms.booking.dto.RoomTypeDTO;
import com.pms.booking.exception.BusinessException;
import com.pms.booking.model.RateModel;
import com.pms.booking.model.RoomTypeModel;
import com.pms.booking.repository.RateRepository;
import com.pms.booking.repository.RoomTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoomTypeService extends GenericService<RoomTypeModel, UUID, RoomTypeDTO> {

    private final RateRepository rateRepository;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, RateRepository rateRepository) {
        super(roomTypeRepository);
        this.rateRepository = rateRepository;
    }

    @Override
    protected RoomTypeModel convertToModel(RoomTypeDTO roomTypeDTO, UUID id) {
        var roomTypeModel = new RoomTypeModel();
        BeanUtils.copyProperties(roomTypeDTO, roomTypeModel);
        roomTypeModel.setId(id);
        return roomTypeModel;
    }

    @Transactional
    @Override
    public RoomTypeModel save(RoomTypeDTO roomTypeDTO) throws BusinessException {
        validateNumberOfMaxPerson(roomTypeDTO.maxPersons());
        var roomTypeModel = new RoomTypeModel();
        BeanUtils.copyProperties(roomTypeDTO, roomTypeModel);
        Optional<RateModel> rateOpc = rateRepository.findById(roomTypeDTO.rateId());

        if (rateOpc.isPresent()) {
            roomTypeModel.setRate(rateOpc.get());
            return repository.save(roomTypeModel);
        } else {
            throw new BusinessException("Tarifa não encontrada para cadastro");
        }
    }

    @Transactional
    @Override
    public RoomTypeModel update(UUID id, RoomTypeDTO roomTypeDTO) throws BusinessException {
        validateNumberOfMaxPerson(roomTypeDTO.maxPersons());
        if (repository.existsById(id)) {
            RoomTypeModel roomTypeModel = convertToModel(roomTypeDTO, id);
            Optional<RateModel> rateOpc = rateRepository.findById(roomTypeDTO.rateId());

            if (rateOpc.isPresent()) {
                roomTypeModel.setRate(rateOpc.get());
                return repository.save(roomTypeModel);
            } else {
                throw new BusinessException("Tarifa não encontrada para cadastro");
            }
        }
        throw new BusinessException("Item não encontrado");
    }

    private void validateNumberOfMaxPerson(int number) throws BusinessException {
        if (number < 1 ) {
            throw new BusinessException("Número máximo de pessoas deve ser maior que 0.");
        }
    }

    public String getRateByRoomType(UUID id) {
        Optional<RoomTypeModel> roomTypeModelOpc = repository.findById(id);

        if (roomTypeModelOpc.isPresent()) {
            return roomTypeModelOpc.get().getRate().getId().toString();
        }
        throw new RuntimeException("Não encontrada tarifa para esta categoria");
    }
}
