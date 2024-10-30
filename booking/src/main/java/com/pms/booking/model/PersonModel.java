package com.pms.booking.model;

import com.pms.booking.enums.EDocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "person", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String document;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "document_type", nullable = false)
    private EDocumentType documentType;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
}