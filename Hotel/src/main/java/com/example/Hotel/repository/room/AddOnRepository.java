package com.example.Hotel.repository.room;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.room.AddOns;


@Repository
public interface AddOnRepository extends JpaRepository<AddOns, Long>{
    
    Optional<AddOns> findByName(String name);
    void deleteByName(String name);
}
