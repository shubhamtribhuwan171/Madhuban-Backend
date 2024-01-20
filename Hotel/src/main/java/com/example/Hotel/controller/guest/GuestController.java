package com.example.Hotel.controller.guest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.guest.GuestDto;
import com.example.Hotel.service.guest.GuestService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/guests")
public class GuestController {
    
    @Autowired
    GuestService guestService;

    //add guest details
    @PutMapping("/add")
    public ResponseEntity<?> addGuestDetails(@RequestBody GuestDto guestDto)
    {
        try
        {
            String response = guestService.addGuest(guestDto);
            return ResponseEntity.ok("Guest Id: "+response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding customer: " + e.getMessage());
        }
    }

    @PutMapping("/editGuest")
    public ResponseEntity<?> updateGuestDetails(@RequestBody GuestDto guestDto, @RequestParam Long guestId)
    {
        try{
        guestService.updateGuestDetails(guestDto, guestId);
        return ResponseEntity.ok("updated "+ guestId);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating guests: "+e.getMessage());
        }
        
    }


    @PutMapping("/addList")
    public ResponseEntity<?> addGuestDetails(@RequestBody List<GuestDto> guest)
    {
        try
        {
            Long response = guestService.addGuestList(guest);
            return ResponseEntity.ok(""+response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding customer: " + e.getMessage());
        }
    }

    @GetMapping("/getByBookingId")
    public ResponseEntity<List<GuestDto>> getByBookingId(@RequestParam Long bookingId)
    {
        List<GuestDto> list = guestService.getByBookingId(bookingId);
        return ResponseEntity.ok(list);
    }
    //get all guest details
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDetails()
    {
        List<GuestDto> list = guestService.getAllGuest();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }


    //add guest
    //update first and last name
    //update doc ids
    //delete ?
}
