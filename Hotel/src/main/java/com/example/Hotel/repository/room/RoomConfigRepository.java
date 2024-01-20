package com.example.Hotel.repository.room;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.room.RoomConfig;


@Repository
public interface RoomConfigRepository extends JpaRepository<RoomConfig, Long>{
    
    Optional<RoomConfig> findByRoomType(String type);
}
