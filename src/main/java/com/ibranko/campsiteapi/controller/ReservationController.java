package com.ibranko.campsiteapi.controller;

import com.ibranko.campsiteapi.exception.InvalidDateException;
import com.ibranko.campsiteapi.exception.ReservationNotFoundException;
import com.ibranko.campsiteapi.model.Reservation;
import com.ibranko.campsiteapi.repository.ReservationRepository;
import com.ibranko.campsiteapi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ibranko.campsiteapi.utils.DateUtils.daysBetween;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/getAll")
    public List<Reservation> getReservations(@RequestBody Map<String, LocalDate> input) {

        //Sets default dates if null
        LocalDate initDate = input.get("initDate") == null ? LocalDate.now().plusDays(1) : input.get("initDate");
        LocalDate endDate = input.get("endDate") == null ? LocalDate.now().plusMonths(1) : input.get("endDate");

        return reservationRepository.findAllByRangeOrderByInitDate(initDate, endDate, Reservation.Status.CONFIRMED);
    }

    @GetMapping("/{bookingId}")
    public Reservation getReservation(@PathVariable("bookingId") UUID bookingId) {
        return reservationRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ReservationNotFoundException(String.format("The entered booking id (%s) was not found", bookingId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation addReservation(@RequestBody Reservation reservation) {
        checkIfReservationIsValid(reservation);

        return reservationRepository.save(reservation);
    }

    @PutMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public Reservation updateReservation(@PathVariable("bookingId") UUID bookingId, @RequestBody Reservation reservation){
        checkIfReservationIsValid(reservation);

        reservationRepository.save(reservation);
        return reservationRepository.findByBookingId(bookingId).get();
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public Reservation cancelReservation(@PathVariable("bookingId") UUID bookingId) {
        Reservation reservationToCancel = reservationRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ReservationNotFoundException(String.format("The entered booking id (%s) was not found", bookingId)));

        reservationToCancel.setStatus(Reservation.Status.CANCELLED);
        return reservationRepository.save(reservationToCancel);
    }

    @GetMapping
    private List<LocalDate> getAvailableDates(@RequestBody Map<String, LocalDate> input) {

        //Sets default dates if null
        LocalDate initDate = input.get("initDate") == null ? LocalDate.now().plusDays(1) : input.get("initDate");
        LocalDate endDate = input.get("endDate") == null ? LocalDate.now().plusMonths(1) : input.get("endDate");

        return findAvailableDates(initDate, endDate);
    }

    private boolean checkIfReservationIsValid(Reservation reservation) {
        LocalDate initDate = reservation.getInitDate();
        LocalDate endDate = reservation.getEndDate();

        //The campsite can be reserved up to 1 month in advance
        if (initDate.isAfter(LocalDate.now().plusMonths(1))) {
            throw new InvalidDateException("You can reserve up to a maximum of 1 month in advance.");
        }

        //The campsite can be reserved for max 3 days
        if (endDate.compareTo(initDate) > 3) {
            throw new InvalidDateException("You can reserve a maximum of 3 days.");
        }

        //The campsite can be reserved minimum 1 day(s) ahead of arrival
        if (initDate.isBefore(LocalDate.now().plusDays(1)) || endDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new InvalidDateException("Reservation date must be tomorrow or later.");
        }

        //If the specified days are not available
        if (!findAvailableDates(initDate, endDate).containsAll(DateUtils.daysBetween(initDate, endDate))) {
            throw new InvalidDateException("The entered date is already reserved.");
        }

        return true;
    }

    private List<LocalDate> findAvailableDates(LocalDate initDate, LocalDate endDate) {

        List<LocalDate> availableDates = daysBetween(initDate, endDate);

        List<Reservation> reservations = reservationRepository.findAllByRangeOrderByInitDate(initDate, endDate, Reservation.Status.CONFIRMED);

        //Removes already reserved days from the list containing available days
        reservations.forEach(r -> availableDates.removeAll(daysBetween(r.getInitDate(), r.getEndDate())));

        return availableDates;
    }
}
