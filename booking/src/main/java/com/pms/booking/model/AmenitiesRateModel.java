package com.pms.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "amenities_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenitiesRateModel extends GenericModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "monday_rate", nullable = false)
    private BigDecimal mondayRate;

    @Column(name = "tuesday_rate", nullable = false)
    private BigDecimal tuesdayRate;

    @Column(name = "wednesday_rate", nullable = false)
    private BigDecimal wednesdayRate;

    @Column(name = "thursday_rate", nullable = false)
    private BigDecimal thursdayRate;

    @Column(name = "friday_rate", nullable = false)
    private BigDecimal fridayRate;

    @Column(name = "saturday_rate", nullable = false)
    private BigDecimal saturdayRate;

    @Column(name = "sunday_rate", nullable = false)
    private BigDecimal sundayRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenities_id", nullable = false)
    private AmenitiesModel amenities;
}
