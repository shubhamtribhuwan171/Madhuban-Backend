package com.example.Hotel.entity.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RoomAvailabilityStatusDto {

    @JsonProperty("room")
    Integer roomNumber;
    @JsonProperty("status")
    String status;//availbale, booked
    @JsonProperty("checkIn")
    LocalDate checkinDate;
    @JsonProperty("checkOut")
    LocalDate checkoutDate;
    @JsonProperty("checkInTime")
    LocalTime checkinTime;
    @JsonProperty("checkOutTime")
    LocalTime checkoutTime;
    //@JsonProperty("customer")
    @JsonIgnore
    String customerName;
    
    @JsonIgnore
    Long bookingId;
    //roomType
    @JsonProperty("roomType")
    String roomType;
    //amenities
    @JsonProperty("amenities")
    String amenities;
    //costperday
    @JsonProperty("costPerDay")
    Double costPerDay;

    //bedType
    @JsonProperty("bedType")
    String bedType;
    //viewType
    @JsonProperty("viewType")
    String viewType;
    //bathroomType
    @JsonProperty("bathroomType")
    String bathroomType;
    //floor
    @JsonProperty("floor")
    String floor;
}