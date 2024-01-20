package com.example.Hotel.repository.room;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.room.Room;
import com.example.Hotel.entity.room.RoomConfig;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
    List<Room> findByRoomType(RoomConfig type);
    List<Room> findByRoomStatus(String status);
    void deleteByRoomNumber(Integer roomNumber);
    Optional<Room> findByRoomNumber(Integer roomNumber);
    List<Room> findByViewType(String view);
    List<Room> findByBedType(String bedType);
    List<Room> findByFloor(String floor);
}
