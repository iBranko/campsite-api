package com.ibranko.campsiteapi.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class Reservation {

    public enum Status {
        CONFIRMED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    UUID bookingId = UUID.randomUUID();

    @Column(name = "status", nullable = false)
    private Status status = Status.CONFIRMED;

    @Column(name = "init_date", nullable = false)
    private LocalDate initDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;
}
