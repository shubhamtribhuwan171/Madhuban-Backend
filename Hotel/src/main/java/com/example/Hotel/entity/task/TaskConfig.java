package com.example.Hotel.entity.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TaskConfig {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;
    
    String taskName;

}
