package com.example.Hotel.controller.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.room.RoomDto;
import com.example.Hotel.service.room.DataInitializationService;
import com.example.Hotel.service.room.RoomService;
import com.example.Hotel.service.room.RoomStatusService;


@RestController
@RequestMapping("/rooms")
public class RoomController {
    
    @Autowired
    RoomService roomService;
    @Autowired
    RoomStatusService roomStatusService;

    @Autowired
    private DataInitializationService dataInitializationService;

    @GetMapping("/load")
    public ResponseEntity<String> initializeDataIfNeeded() {
        dataInitializationService.initializeDataIfNeeded();
        return ResponseEntity.ok("Data initialization completed or already initialized.");
    }

    //add rooms
    @PutMapping("/add")
    public ResponseEntity<?> addRoom(@RequestBody RoomDto roomDto)
    {
        try
        {
            roomService.addRoom(roomDto);
            return ResponseEntity.ok("Room added successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Room: "+ e.getMessage());
        }
    }

    @GetMapping("/getAll")
    //get rooms
    public ResponseEntity<List<RoomDto>> getAllRooms()
    {
        List<RoomDto> list = roomService.getAllRooms();
        return ResponseEntity.ok(list);
    }

    //get rooms by floor
    @GetMapping("/getByFloor")
    public ResponseEntity<List<RoomDto>> getByFloor(@RequestParam String floor)
    {
        List<RoomDto> list = roomService.getByFloor(floor);
        return ResponseEntity.ok(list);
    }
    //get by view
    @GetMapping("/getByView")
    public ResponseEntity<List<RoomDto>> getByView(@RequestParam String view)
    {
        List<RoomDto> list = roomService.getByView(view);
        return ResponseEntity.ok(list);
    }
    
    //get by bed
    @GetMapping("/getByBed")
    public ResponseEntity<List<RoomDto>> getByBed(@RequestParam String bed)
    {
        List<RoomDto> list = roomService.getByBed(bed);
        return ResponseEntity.ok(list);
    }

    //get all isActive,
    //underMaintenance,
    //cleaningRequired
    @GetMapping("/getByStatus")
    public ResponseEntity<List<RoomDto>> getByStatus(@RequestParam String statusString)
    {
        List<RoomDto> list = roomService.getByStatus(statusString);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/deleteByRoomNumber")
    //delete room by room Number
    public ResponseEntity<?> deleteByRoomNumber(@RequestParam Integer roomNumber)
    {
        try
        {
            roomService.deleteByRoomNumber(roomNumber);
            return ResponseEntity.ok("Room Deleted successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Deleting Room: "+ e.getMessage());
        }
    }

    //get by room number
    @GetMapping("/getByRoomNumber")
    public ResponseEntity<?> getByRoomNumber(@RequestParam Integer roomNumber)
    {
        return ResponseEntity.ok(roomService.getByRoomNumber(roomNumber));
    }

    


  /*   //update status
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestParam Integer roomNumber, @RequestParam String newStatusString, @RequestParam(required = false) String checkInf,
            @RequestParam(required = false) String checkOutf) {

        try {
            
            roomService.updateStatus(newStatusString, roomNumber, LocalDate.parse(checkInf), LocalDate.parse(checkOutf));
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Updating Room: " + e.getMessage());
        }
    }*/
}