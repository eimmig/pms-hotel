package com.pms.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "booking_room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRoomModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private RoomModel room;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private BookingModel booking;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(length = 1, nullable = false)
    private String status;

    @Column(name = "booking_value")
    private BigDecimal bookingValue;

    @Column(name = "amenities_value")
    private BigDecimal amenitiesValue;
}