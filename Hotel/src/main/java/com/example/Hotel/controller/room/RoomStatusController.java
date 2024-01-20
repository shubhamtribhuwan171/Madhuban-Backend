package com.example.Hotel.controller.room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.room.RoomAvailabilityStatus;
import com.example.Hotel.entity.room.RoomAvailabilityStatusDto;
import com.example.Hotel.service.room.RoomStatusService;



@RestController
@RequestMapping("/roomStatus")
public class RoomStatusController {
    
    @Autowired
    RoomStatusService roomStatusService;

    
    @PutMapping("/checkIn")
    public ResponseEntity<String> checkInStatus(@RequestParam Long bookingId)
    {
        try{
        roomStatusService.checkInStatus(bookingId);
        return ResponseEntity.ok("checked in!!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Checking in: " + e.getMessage());
        }
    }

    
    @PutMapping("/checkOut")
    public ResponseEntity<String> checkOutStatus(@RequestParam Long bookingId)
    {
        try{
        roomStatusService.checkOutStatus(bookingId);
        return ResponseEntity.ok("checked out");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking out: " + e.getMessage());
        }
    }

    //getAll
    /*@GetMapping("/getAllRooms")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getAllRooms()
    {
        return ResponseEntity.ok(roomStatusService.getAllRooms());
    }*/

    //get all rooms with customer name
    @GetMapping("/getAll")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getAllRoomsWithCustomer()
    {
        return ResponseEntity.ok(roomStatusService.getAllRoomsWithCustomer());
    }


    @GetMapping("/getDueOut")
    //working
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getRoomsDueOut(@RequestParam(defaultValue = "24") int hours)
    {
        List<RoomAvailabilityStatusDto> list = roomStatusService.getRoomsDueOutNextHours(LocalDate.now(), LocalTime.now(), hours);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getDirtyRooms")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getDirty()
    {
        List<RoomAvailabilityStatusDto> list = roomStatusService.getDirtyRooms(LocalDate.now());
        return ResponseEntity.ok(list);
    }
    
    /*working
    @GetMapping("/getCurrentVacant")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getCurrentVacant()
    {
        List<RoomAvailabilityStatusDto> list = roomStatusService.getCurrentlyVacantRooms();
        return ResponseEntity.ok(list);
    }*/

    //not working
    @GetMapping("/getOutOfOrder")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getOutOfOrder()
    {
        List<RoomAvailabilityStatusDto> list = roomStatusService.getOutOfOrderRooms();
        return ResponseEntity.ok(list);
    }

    //working
    @GetMapping("/getReserved")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getReserved(@RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate)
    {
        if(checkInDate == null && checkOutDate == null)
        {
            checkInDate = LocalDate.now();
            checkOutDate = LocalDate.now().plusMonths(1);
        }
        if(checkOutDate == null)
            checkOutDate = LocalDate.now().plusMonths(1);
        List<RoomAvailabilityStatusDto> list = roomStatusService.getReservedRoomsForDateRange(checkInDate, checkOutDate);
        return ResponseEntity.ok(list);
    }

    //not working
    @GetMapping("/getAllReserved")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getAllReserved()
    {
 
        List<RoomAvailabilityStatusDto> list = roomStatusService.getAllReservedRooms();
        return ResponseEntity.ok(list);
    }


    //working
    @GetMapping("/getVacantBetween")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getVacant(@RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate)
    {
        if(checkInDate == null && checkOutDate == null)
        {
            checkInDate = LocalDate.now();
            checkOutDate = LocalDate.now().plusDays(1);
        }
        if(checkOutDate == null)
            checkOutDate = checkInDate.plusDays(1);
        List<RoomAvailabilityStatusDto> list = roomStatusService.getVacantRoomsForDateTimeRange(checkInDate,LocalTime.now(), checkOutDate, LocalTime.now());
        return ResponseEntity.ok(list);
    }

    //not working
    @GetMapping("/getOccupied")
    public ResponseEntity<List<RoomAvailabilityStatus>> getOccupied(@RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate)
    {
        if(checkInDate == null && checkOutDate == null)
        {
            checkInDate = LocalDate.now();
            checkOutDate = LocalDate.now().plusDays(1);
        }
        if(checkOutDate == null)
            checkOutDate = LocalDate.now().plusDays(1);
        //List<RoomAvailabilityStatus> list = roomStatusService.getReservedRoomsForDateRange(checkInDate, checkOutDate);
        List<RoomAvailabilityStatus> list = roomStatusService.getOccupiedRooms();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getUpcomingCheckIn")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getUpcomingCheckIns(@RequestParam(required = false) LocalDate date) {

        if(date == null)
            date = LocalDate.now();
        List<RoomAvailabilityStatusDto> list = roomStatusService.getUpcomingCheckIns(date);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getUpcomingCheckOut")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getUpcomingCheckOuts(@RequestParam(required = false) LocalDate date) {

        if(date == null)
            date = LocalDate.now();
        List<RoomAvailabilityStatusDto> list = roomStatusService.getUpcomingCheckout(date);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getNextCheckIn")
    public ResponseEntity<RoomAvailabilityStatusDto> getNextCheckInForRoom(@RequestParam Integer room,@RequestParam(required = false) LocalDate currentDate) {

        RoomAvailabilityStatusDto ras =  roomStatusService.getNextCheckInForRoom(room, currentDate);
        return ResponseEntity.ok(ras);
    }

    //get current status of all rooms
    @GetMapping("/currentStatus")
    public ResponseEntity<List<RoomAvailabilityStatusDto>> getCurrentStatusForAllRooms(@RequestParam(required = false) LocalDate date) {
        if(date == null)
            date = LocalDate.now();
        List<RoomAvailabilityStatusDto> roomStatusList = roomStatusService.getCurrentStatusForAllRooms(date);
        return ResponseEntity.ok(roomStatusList);
    }

}
