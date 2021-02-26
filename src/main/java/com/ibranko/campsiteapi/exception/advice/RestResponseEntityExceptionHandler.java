package com.ibranko.campsiteapi.exception.advice;

import com.ibranko.campsiteapi.exception.InvalidDateException;
import com.ibranko.campsiteapi.exception.ReservationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ReservationNotFoundException.class)
    ResponseEntity<Object> reservationNotFound(ReservationNotFoundException ex) {
        return getObjectResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(InvalidDateException.class)
    ResponseEntity<Object> invalidDate(InvalidDateException ex) {
        return getObjectResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    private ResponseEntity<Object> getObjectResponseEntity(HttpStatus httpStatus, String message, RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", httpStatus);
        body.put("message", message);

        return new ResponseEntity<>(body, httpStatus);
    }

}
