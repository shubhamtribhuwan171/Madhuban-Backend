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
@Table(name = "add_ons")
@RequiredArgsConstructor
public class AddOns {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addon_id")
    Long id;
    
    @Column(name = "name")
    String name;

    @Column(name = "cost")
    Double cost;

    public AddOns(String name, Double cost)
    {
        this.name = name;
        this.cost = cost;
    }

}
