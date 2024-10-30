package com.pms.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "booking_room_amenities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRoomAmenitiesModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_room_id", nullable = false)
    private BookingRoomModel bookingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenities_id", nullable = false)
    private AmenitiesModel amenities;
}
