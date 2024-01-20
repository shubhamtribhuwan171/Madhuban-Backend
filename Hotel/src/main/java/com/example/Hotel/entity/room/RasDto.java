package com.example.Hotel.entity.room;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.reservation.Booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
//this class is for showing all the rooms with customer
public class RasDto {
    private Long id;
    private Room room;
    private String status;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private LocalTime checkinTime;
    private LocalTime checkoutTime;
    private Booking booking;
    private Customer customer;
}
