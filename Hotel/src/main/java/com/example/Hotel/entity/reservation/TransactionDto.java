package com.example.Hotel.entity.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
    
    @JsonProperty("amountPaid")
    Double amountPaid;
    @JsonProperty("paymentMode")
    String paymentMode;

    @JsonProperty("bookingId")
    Long bookingId;

    @JsonProperty("date")
    LocalDate date;

}
