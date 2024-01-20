package com.example.Hotel.entity.room;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomStatusChangeEvent {

    private final Room room;
    private final String newStatus;

    LocalDate checkInDate;
    LocalDate checkOutDate;
    LocalTime checkInTime;
    LocalTime checkOutTime;

    public RoomStatusChangeEvent(Room room, String newStatus, LocalDate checkIn, LocalDate checkOut, LocalTime checkInTime, LocalTime checkOutTime) {
        this.room = room;
        this.newStatus = newStatus;
        
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        
        this.checkOutTime = checkOutTime;
        this.checkInTime = checkInTime;
    }

    public Room getRoom() {
        return room;
    }

    public String getNewStatus() {
        return newStatus;
    }
}

