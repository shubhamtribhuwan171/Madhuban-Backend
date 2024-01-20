package com.example.Hotel.entity.room;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

    @JsonProperty("roomNumber")
    private Integer roomNumber;
    @JsonProperty("roomType")
    private String roomType;
    @JsonProperty("roomStatus")
    String roomStatus;
    @JsonProperty("bed")
    String bedType;
    @JsonProperty("view")
    String viewType;
    @JsonProperty("bathroom")
    String bathroom;
    @JsonProperty("floor")
    String floor;

    /*public RoomDto(Integer roomNumber, String roomType, String status)
    {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = status;
    }*/

    public RoomDto(Integer roomNumber, String roomType, String status, String bed, String view, String bathroom, String floor)
    {
        this.bathroom = bathroom;
        this.bedType = bed;
        this.viewType = view;
        this.floor = floor;

        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = status;

    }
}
