package com.ibranko.campsiteapi.repository;

import com.ibranko.campsiteapi.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r where r.initDate >= ?1 and r.endDate <= ?2 and r.status = ?3")
    List<Reservation> findAllByRangeOrderByInitDate(LocalDate initDate, LocalDate endDate, Reservation.Status status);

    //List<Reservation> findAllByInitDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate initDate, LocalDate EndDate);

    Reservation findByBookingId(UUID bookingId);
}
