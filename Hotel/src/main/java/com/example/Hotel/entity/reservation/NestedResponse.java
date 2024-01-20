package com.example.Hotel.entity.reservation;

import java.util.List;

import com.example.Hotel.entity.guest.CustomerDto;
import com.example.Hotel.entity.guest.GuestDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NestedResponse {
    @JsonProperty("customer")
    CustomerDto cust;
    @JsonProperty("booking")
    BookingDto booking;
    @JsonProperty("guest")
    List<GuestDto> guests;

}
