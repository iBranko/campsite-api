package com.ibranko.campsiteapi.exception;

public class InvalidReservationStatusException extends RuntimeException {

    public InvalidReservationStatusException(String message) {
        super(message);
    }
}
