package com.example.Hotel.entity.reservation;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingSummaryDto {
    //booking id
    Long bookingId;
    LocalDate checkIn;
    LocalDate checkOut;
    LocalTime checkInTime;
    LocalTime checkOutTime;
    
    Double addOnTotal;
    Double total;
    Double paidAmt;
    Double pendingAmt;

    //customer details: fname, lname, age, docId
    String customerName;

    Map<String, Integer> addOnMap = new HashMap<>();
    //guest details: fName, lastName,age, docId
    List<String> guestList = new ArrayList<>();
    
    //room details: roomNumber
    Integer roomNumber;
    //room config: roomType, Amenities
    String roomType;
    String amenities;

    public BookingSummaryDto()
    {

    }

    
}
