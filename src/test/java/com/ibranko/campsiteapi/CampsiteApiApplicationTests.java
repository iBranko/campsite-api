package com.ibranko.campsiteapi;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CampsiteApiApplicationTests {

    @Test
    void givenBookingIdDoestNotExists_whenReservationIsRetrieved_then404IsReceived() throws IOException {
        //Given
        UUID bookingId = UUID.randomUUID();
        HttpUriRequest request = new HttpGet("http://localhost:8080/reservations/" + bookingId);

        //When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        //Then
        Assertions.assertEquals(httpResponse.getStatusLine().getStatusCode(),
                HttpStatus.NOT_FOUND.value());
    }

}
