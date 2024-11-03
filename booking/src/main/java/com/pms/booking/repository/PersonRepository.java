package com.pms.booking.repository;

import com.pms.booking.enums.EDocumentType;
import com.pms.booking.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<PersonModel, UUID> {
    Optional<PersonModel> findByDocumentAndDocumentType(String document, EDocumentType documentType);

    Optional<PersonModel> findByName(String name);

    Optional<PersonModel> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT p.* " +
            "FROM person p " +
            "INNER JOIN booking b ON b.person_id = p.id " +
            "WHERE b.status = 'P'",
            nativeQuery = true)
    List<PersonModel> getForCheckin();


    @Query(value = "SELECT p.* " +
            "FROM person p " +
            "INNER JOIN booking b ON b.person_id = p.id " +
            "WHERE b.status = 'E'",
            nativeQuery = true)
    List<PersonModel> getForCheckout();

    Boolean existsByDocumentAndDocumentType(String document, EDocumentType documentType);
    Boolean existsByEmail(String email);
}
