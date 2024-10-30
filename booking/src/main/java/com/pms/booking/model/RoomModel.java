package com.pms.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id", nullable = false)
    private RoomTypeModel roomType;

    @Column(length = 1, nullable = false)
    private String status;
}