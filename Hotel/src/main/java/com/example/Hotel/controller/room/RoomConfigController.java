package com.example.Hotel.controller.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.room.RoomConfigDto;
import com.example.Hotel.service.room.DataInitializationService;
import com.example.Hotel.service.room.RoomConfigServices;


@RestController
@RequestMapping("/roomConfig")
public class RoomConfigController {

    @Autowired
    RoomConfigServices roomConfigService;

    @Autowired
    private DataInitializationService dataInitializationService;

    @GetMapping("/load")
    public ResponseEntity<String> initializeDataIfNeeded() {
        dataInitializationService.initializeDataIfNeeded();
        return ResponseEntity.ok("Data initialization completed or already initialized.");
    }

    
    @PutMapping("/add")
    //add room type
    public ResponseEntity<?> addRoomConfig(@RequestBody RoomConfigDto roomConfigDto)
    {
        try
        {
            roomConfigService.addRoomConfig(roomConfigDto);
            return ResponseEntity.ok("Room Type added successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Room Type: "+ e.getMessage());
        }
    }

    //add amenities
    @PutMapping("/addAmenities")
    public ResponseEntity<?> addRoomAmenities(@RequestParam String newAmenities,@RequestParam String roomType)
    {
        try
        {
            roomConfigService.addRoomAmenities(newAmenities, roomType);
            return ResponseEntity.ok("Amenities added successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Amenities: "+ e.getMessage());
        }
    }

    //delete amenities
    @PutMapping("/delete")
    public ResponseEntity<?> deleteAmenities(@RequestParam String amenityToRemove,@RequestParam String roomType)
    {
        try
        {
            roomConfigService.deleteAmenities(amenityToRemove, roomType);
            return ResponseEntity.ok("Amenities Deleted Successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Deleteing Amenities: "+ e.getMessage());
        }
    }

    
    //update cost//working
    @PutMapping("/updateCost")
    public ResponseEntity<?> updateCost(@RequestParam String roomType, @RequestParam String costString)
    {
        try
        {
            roomConfigService.updateCost(roomType, costString);
            return ResponseEntity.ok("Cost updated Successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Cost: "+ e.getMessage());
        }
    }

    //read all types
    @GetMapping("/getAll")//working
    public ResponseEntity<List<RoomConfigDto>> getAllRoomTypes()
    {
        List<RoomConfigDto> list = roomConfigService.getAllRoomTypes();
        return ResponseEntity.ok(list);
    }
    
    //get a room type details//working
    @GetMapping("/getByRoomType")
    public ResponseEntity<RoomConfigDto> getByRoomType(@RequestParam String type)
    {
        RoomConfigDto roomConfigDto = roomConfigService.getByRoomType(type);
        return ResponseEntity.ok(roomConfigDto);
    }
}
