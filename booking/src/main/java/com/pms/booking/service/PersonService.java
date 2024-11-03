package com.pms.booking.service;

import com.pms.booking.dto.PersonDTO;
import com.pms.booking.dto.PersonReciveDTO;
import com.pms.booking.dto.RoomReciveListDTO;
import com.pms.booking.enums.EDocumentType;
import com.pms.booking.exception.BusinessException;
import com.pms.booking.model.PersonModel;
import com.pms.booking.model.RoomModel;
import com.pms.booking.repository.PersonRepository;
import com.pms.booking.utils.DocumentFormatter;
import com.pms.booking.utils.DocumentValidator;
import com.pms.booking.utils.PhoneFormateter;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService extends GenericService<PersonModel, UUID, PersonDTO> {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        super(personRepository);
        this.personRepository = personRepository;
    }

    @Override
    protected PersonModel convertToModel(PersonDTO personDTO, UUID id) {
        var personModel = new PersonModel();
        BeanUtils.copyProperties(personDTO, personModel);
        personModel.setId(id);
        personModel.setPhoneNumber(PhoneFormateter.unformatPhoneNumber(personDTO.phoneNumber()));
        personModel.setDocument(DocumentFormatter.unformatDocument(personDTO.document(), personDTO.documentTypeId()));
        personModel.setDocumentType(EDocumentType.fromCode(personDTO.documentTypeId()));
        return personModel;
    }

    @Transactional
    public PersonModel save(PersonDTO personDTO) throws RuntimeException {
        PersonModel personModel = convertToModel(personDTO, null);

        return validateAndSavePersonData(personModel);
    }

    @Override
    @Transactional
    public PersonModel update(UUID id, PersonDTO personDTO) throws RuntimeException {
        if (repository.existsById(id)) {
            PersonModel personModel = convertToModel(personDTO, id);

            return validateAndSavePersonData(personModel);
        }
        throw new RuntimeException("Item não encontrado");
    }

    private PersonModel validateAndSavePersonData(PersonModel personModel) {
        if (!DocumentValidator.validateDocument(personModel.getDocument(), personModel.getDocumentType())) {
            throw new ValidationException("O documento informado não é válido!");
        }

        if (personRepository.existsByDocumentAndDocumentType(personModel.getDocument(), personModel.getDocumentType())) {
            throw new BusinessException("Documento já cadastrado na base de dados");
        } else if (personRepository.existsByEmail(personModel.getEmail())) {
            throw  new BusinessException("Email já cadastrado na base de dados");
        }

        return repository.save(personModel);
    }

    public Optional<PersonModel> getPerson(String search) {
        Optional<PersonModel> personByName = personRepository.findByName(search);
        if (personByName.isPresent()) {
            return personByName;
        }

        EDocumentType documentType;
        if (search.length() == 11) {
            documentType = EDocumentType.CPF;
        } else if (search.length() == 14) {
            documentType = EDocumentType.CNPJ;
        } else {
            documentType = EDocumentType.PASSPORT;
        }

        String formattedDocument = DocumentFormatter.unformatDocument(search, documentType.getCode());
        Optional<PersonModel> personByDocument = personRepository.findByDocumentAndDocumentType(formattedDocument, documentType);

        if (personByDocument.isPresent()) {
            return personByDocument;
        }

        return personRepository.findByPhoneNumber(search);
    }

    public List<PersonModel> getForCheckin() {
        return personRepository.getForCheckin();
    }

    public List<PersonModel> getForCheckout() {
        return personRepository.getForCheckout();
    }

    public List<PersonReciveDTO> getAllBooking() {
        List<PersonModel> personModels = repository.findAll();

        return personModels.stream()
                .map(person -> new PersonReciveDTO(person.getId(), person.getName()))
                .collect(Collectors.toList());
    }
}
