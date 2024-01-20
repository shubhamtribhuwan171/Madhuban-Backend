package com.example.Hotel.entity.reservation;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionResponse {

    @JsonProperty("transactionId")
    Long tranasctionId;
    @JsonProperty("bookingId")
    Long bookingId ;
    @JsonProperty("billingId")
    Long billId;
    @JsonProperty("paymentMode")
    String paymentMode;
    @JsonProperty("paymentDate")
    LocalDate paymentDate;
    @JsonProperty("amountPaid")
    Double amountPaid;
    
}
