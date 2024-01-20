package com.example.Hotel.entity.room;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomConfigDto {
    
    @JsonProperty("roomType")
    String roomType;//AC, Non - Ac, Deluxe
    @JsonProperty("amenities")
    String amenities;
    @JsonProperty("costPerDay")
    String costPerDay;//convert this into Double, this is in string

    public RoomConfigDto(String roomType, String amenities, String cost)
    {
        this.roomType = roomType;
        this.amenities = amenities;
        this.costPerDay = cost;
    }
}
