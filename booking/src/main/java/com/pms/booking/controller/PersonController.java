package com.pms.booking.controller;

import com.pms.booking.dto.PersonDTO;
import com.pms.booking.dto.PersonReciveDTO;
import com.pms.booking.dto.RoomReciveListDTO;
import com.pms.booking.model.PersonModel;
import com.pms.booking.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/person")
public class PersonController extends GenericController<PersonModel, UUID, PersonDTO> {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        super(personService);
        this.personService = personService;
    }

    @GetMapping("/getPerson/{search}")
    public ResponseEntity<PersonModel> getPerson(@PathVariable String search) {
        Optional<PersonModel> item = personService.getPerson(search);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getForCheckin")
    public ResponseEntity<List<PersonModel>> getForCheckin() {
        List<PersonModel> items = personService.getForCheckin();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/getForCheckout")
    public ResponseEntity<List<PersonModel>> getForCheckout() {
        List<PersonModel> items = personService.getForCheckout();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/toBooking")
    public ResponseEntity<List<PersonReciveDTO>> getAllBooking() {

        List<PersonReciveDTO> list = personService.getAllBooking();

        if (list != null) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

