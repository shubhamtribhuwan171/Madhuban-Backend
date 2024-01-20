package com.example.Hotel.entity.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BillingDto {
 
    Long billingId;

    Long bookingId;

    @JsonProperty("total_room_charges")
    Double totalRoomCharges;
    @JsonProperty("total_addon_charges")
    Double totalAddOnCharges;
    @JsonProperty("paid_amount")
    Double paidAmount;
    @JsonProperty("pending_amount")
    Double pendingAmount;
    @JsonProperty("checkin_date")
    LocalDate checkinDate;
    @JsonProperty("checkout_date")
    LocalDate checkoutDate;

    @JsonProperty("checkin_time")
    LocalTime checkinTime;
    @JsonProperty("checkout_time")
    LocalTime checkoutTime;

    @JsonProperty("transactions")
    List<String> transactions;
}
