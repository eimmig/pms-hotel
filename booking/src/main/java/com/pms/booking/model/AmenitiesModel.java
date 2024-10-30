package com.pms.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "amenities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenitiesModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
}
