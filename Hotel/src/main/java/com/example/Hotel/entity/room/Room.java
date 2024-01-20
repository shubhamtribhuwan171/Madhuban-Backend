package com.example.Hotel.entity.room;

import java.util.ArrayList;
import java.util.List;

import com.example.Hotel.entity.task.Task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "room")
@RequiredArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(name = "room_number")
    private Integer roomNumber;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomConfig roomType;

    @Column(name = "room_status")//vacant/ undermaintainance/ reserved/ occupied/ dueOut/ dirty
    String roomStatus;

    @Column(name = "bed")
    String bedType; //single/ double

    @Column(name = "view")
    String viewType; //parking/ balcony/ mountain

    @Column(name = "bathroom")
    String bathroomType;

    @Column(name = "floor")
    String floor;

    @OneToMany(mappedBy = "room") // Refers to the "room" field in the Task class
    @Column(name = "tasks")
    private List<Task> tasks = new ArrayList<>();

    public Room(Integer roomNumber, RoomConfig roomType, String status, String bed, String view, String bathroom, String floor)
    {
        this.roomNumber = roomNumber;

        this.roomType = roomType;
        this.roomStatus = status;

        this.bathroomType = bathroom;
        this.bedType = bed;
        this.viewType = view;
        this.floor = floor;
    }

}
