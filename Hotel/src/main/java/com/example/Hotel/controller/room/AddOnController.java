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

import com.example.Hotel.entity.room.AddOnsDto;
import com.example.Hotel.service.room.AddOnService;


@RestController
@RequestMapping("/addOns")
public class AddOnController {

    @Autowired
    AddOnService addOnService;
    
    //add new addOn//working
    @PutMapping("/add")
    public ResponseEntity<?> addAddOn(@RequestBody AddOnsDto addOnsDto)
    {
        try
        {
            addOnService.addAddOn(addOnsDto);
            return ResponseEntity.ok("Customer added successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Add Ons: "+ e.getMessage());
        }
    }

    //get all add ons available//working
    @GetMapping("/getAll")
    public ResponseEntity<List<AddOnsDto>> getAllAddOns()
    {
        List<AddOnsDto> list = addOnService.getAllAddOns();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/getCost")
    //get cost by add name//working
    public ResponseEntity<?> getCostByAddOnName(@RequestParam String addOnName)
    {
        Double cost = addOnService.getCostByAddOnName(addOnName);
        return ResponseEntity.ok(cost);
    }

    @PutMapping("/updateCost")
    //update Cost by add on name//working
    public ResponseEntity<?> updateCostByAddOnName(@RequestParam String addOnName,@RequestParam Double newCost)
    {
        try
        {
            addOnService.updateCostByAddOnName(addOnName, newCost);
            return ResponseEntity.ok("Cost Updated successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Updating Cost: "+ e.getMessage());
        }
    }
    @PutMapping("/delete")
    //delete add on by name
    public ResponseEntity<?> deleteByAddOnName(@RequestParam String addOnName)
    {
        try
        {
            addOnService.deleteByAddOnName(addOnName);
            return ResponseEntity.ok("AddOn Deleted successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Deleting AddOn: "+ e.getMessage());
        }
    }
    
}
