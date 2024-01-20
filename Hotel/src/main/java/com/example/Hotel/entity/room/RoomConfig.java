package com.example.Hotel.entity.room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "room_config")
@RequiredArgsConstructor
public class RoomConfig {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    @Column(name = "roomType")
    String roomType;//AC, Non - Ac, Deluxe
    @Column(name = "amenities")
    String amenities;
    @Column(name = "costPerDay")
    Double costPerDay;

    public RoomConfig(String roomType, String amenities, Double cost)
    {
        this.roomType = roomType;
        this.amenities = amenities;
        this.costPerDay = cost;
    }
    
    public void addAmenities(String newAmenities)
    {
        this.amenities.concat(" "+newAmenities);
    }

    public void deleteAmenities(String amenityToRemove)
    {
        this.amenities.replace(amenityToRemove, "");
        this.amenities.replace(amenityToRemove + " ", "");
    }
}
