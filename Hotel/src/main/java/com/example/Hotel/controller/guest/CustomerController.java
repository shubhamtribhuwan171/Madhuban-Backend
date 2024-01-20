package com.example.Hotel.controller.guest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.guest.CustomerDto;
import com.example.Hotel.service.guest.CustomerService;
import com.example.Hotel.service.reservation.BookingService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    CustomerService customerService;

    @Autowired
    BookingService bookingService;

    //update customer details
    @PutMapping("/editCustomer")
    public ResponseEntity<?> updateCustomerDetails(@RequestBody CustomerDto custDto, @RequestParam Long customerId)
    {
        try{
            
        customerService.updateCustomerDetails(custDto, customerId);
        return ResponseEntity.ok("customer details updated "+ customerId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating customer: " + e.getMessage());
        }
        
    }

    //add cutomer details
    @PostMapping("/add")
    public ResponseEntity<String> addCustomerDetails(@RequestBody CustomerDto customerDto)
    {
        try
        {
            Long response = customerService.addCustomer(customerDto);
            //return ResponseEntity.ok(bookingId+"");
            return ResponseEntity.ok("CustomerId:"+response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding customer: " + e.getMessage());
        }
    }

    //get all customers
    @GetMapping("/getAll")
    public List<?> getAllDetails()
    {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteById(@RequestParam Long id)
    {
        try
        {
            String response = customerService.deleteCust(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding customer: " + e.getMessage());
        }
    }
}
    