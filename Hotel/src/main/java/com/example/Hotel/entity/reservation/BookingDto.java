package com.example.Hotel.entity.reservation;


import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BookingDto {
    
    Long bookingId;
    private Integer roomNumber;

    private Long custId;

    int totalGuestCount;

    String checkIn;
    String checkOut;
    String checkInTime;
    String checkOutTime;

    Map<String, Integer> addOnMap = new HashMap<>();
    //String paymentStatus;


    public BookingDto(Integer roomNumber, Long custId, int totalGuestCount, String checkInString,
        String checkOutString, String checkInTime, String checkOutTime, Map<String, Integer> addOnMap)
    {
        this.roomNumber = roomNumber;
        this.custId = custId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        if(addOnMap != null)
            this.addOnMap.putAll(addOnMap);
        this.checkIn = checkInString;
        this.checkOut = checkOutString;
        this.totalGuestCount = totalGuestCount;
        
    }


}
