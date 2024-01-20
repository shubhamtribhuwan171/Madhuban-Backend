package com.example.Hotel.entity.room;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddOnsDto {
    
    @JsonProperty("name")
    String name;
    @JsonProperty("cost")
    Double cost;

    public AddOnsDto(String name, String cost)
    {
        this.name = name;
        this.cost = Double.parseDouble(cost);
    }
}
