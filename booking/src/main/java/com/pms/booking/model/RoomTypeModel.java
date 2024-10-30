package com.pms.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "room_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_persons", nullable = false)
    private Integer maxPersons;

    @Column(nullable = false)
    private String abbreviation;

    @ManyToOne
    @JoinColumn(name = "rate_id", referencedColumnName = "id", nullable = false)
    private RateModel rate;
}
