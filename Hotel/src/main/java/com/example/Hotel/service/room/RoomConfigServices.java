package com.example.Hotel.service.room;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.room.RoomConfig;
import com.example.Hotel.entity.room.RoomConfigDto;
import com.example.Hotel.repository.room.RoomConfigRepository;

import jakarta.transaction.Transactional;

@Service
public class RoomConfigServices {
    
    @Autowired
    RoomConfigRepository roomConfigRepository;

    //initialise room types
    
    @Transactional
    //add room type
    public void addRoomConfig(RoomConfigDto roomTypeDto)
    {
        RoomConfig roomConfig = mapToObj(roomTypeDto);
        roomConfigRepository.save(roomConfig);
    }

    @Transactional
    //add amenities
    public void addRoomAmenities(String newAmenities, String roomType)
    {
        Optional<RoomConfig> optional = roomConfigRepository.findByRoomType(roomType);
        if(optional.isPresent())
        {
            RoomConfig roomConfig = optional.get();
            roomConfig.addAmenities(newAmenities);
            roomConfigRepository.save(roomConfig);
        }
    }

    @Transactional
    //delete amenities
    public void deleteAmenities(String amenityToRemove, String roomType)
    {
        Optional<RoomConfig> optional = roomConfigRepository.findByRoomType(roomType);
        if(optional.isPresent())
        {
            RoomConfig roomConfig = optional.get();
            roomConfig.deleteAmenities(amenityToRemove);
            roomConfigRepository.save(roomConfig);
        }
    }

    @Transactional
    //update cost
    public void updateCost(String roomType, String costString)
    {
        Double cost = Double.parseDouble(costString);
        Optional<RoomConfig> optional = roomConfigRepository.findByRoomType(roomType);
        if(optional.isPresent())
        {
            RoomConfig roomConfig = optional.get();
            roomConfig.setCostPerDay(cost);
            roomConfigRepository.save(roomConfig);
        }

    }

    //read all types
    public List<RoomConfigDto> getAllRoomTypes()
    {
        List<RoomConfig> list = roomConfigRepository.findAll();
        return mapToDtoList(list);
    }
    
    //get a room type details
    public RoomConfigDto getByRoomType(String type)
    {
        Optional<RoomConfig> optional = roomConfigRepository.findByRoomType(type);

        if(optional.isPresent())
        {
            RoomConfig roomConfig = optional.get();
            return mapToDto(roomConfig);
        }
        return null;
    }

    //map dto to obj
    private static RoomConfig mapToObj(RoomConfigDto roomConfigDTo)
    {
        String roomtype = roomConfigDTo.getRoomType();
        String amenities = roomConfigDTo.getAmenities();
        Double cost = Double.parseDouble(roomConfigDTo.getCostPerDay());
        RoomConfig roomConfig = new RoomConfig(roomtype, amenities, cost);
        return roomConfig;
    }
    //map obj to dto
    private static RoomConfigDto mapToDto(RoomConfig roomConfig)
    {
        String type = roomConfig.getRoomType();
        String amenities = roomConfig.getAmenities();
        Double cost = roomConfig.getCostPerDay();
        RoomConfigDto roomConfigDto = new RoomConfigDto(type, amenities, cost.toString());
        return roomConfigDto;
    }

    //Map obj List to dto List
    private static List<RoomConfigDto> mapToDtoList(List<RoomConfig> list)
    {
        return list.stream()
            .map(RoomConfigServices :: mapToDto)
            .collect(Collectors.toList());
    }

    //update amenities
    //map dto List to obj list
    
}
