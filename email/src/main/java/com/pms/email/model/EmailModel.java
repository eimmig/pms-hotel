package com.pms.email.model;

import com.pms.email.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "email_from")
    private String emailFrom;

    @Column(name = "email_to")
    private String emailTo;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_email")
    private StatusEmail statusEmail;
}
