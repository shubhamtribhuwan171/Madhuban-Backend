package com.example.Hotel.controller.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.reservation.BillingDto;
import com.example.Hotel.entity.reservation.TransactionResponse;
import com.example.Hotel.service.reservation.BillingService;

@RestController
@RequestMapping("/billing")
public class BillingController {
    
    @Autowired
    BillingService billingService;

    @PutMapping("/create")
    public ResponseEntity<?> createBill(@RequestParam Long bookingId)
    {
        try
        {
        Long id = billingService.createBill(bookingId);
        return ResponseEntity.ok("Bill generated Bill Id: " + id);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating bill: " + e.getMessage());
        }
    }

    @GetMapping("/getBill")
    public ResponseEntity<?> getBill(@RequestParam Long bookingId) throws Exception
    {
        BillingDto bill = billingService.getBill(bookingId);
        return ResponseEntity.ok(bill);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBill(@RequestParam Long bookingId)
    {
        try{
        billingService.deleteBill(bookingId);
        return ResponseEntity.ok("Bill Deleted!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting bill: " + e.getMessage());
        }
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@RequestParam Long bookingId) throws Exception
    {
        List<TransactionResponse> list = billingService.getTransactions(bookingId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getFullyPaid")
    public ResponseEntity<List<BillingDto>> getFullyPaid()
    {
        List<BillingDto> list = billingService.getFullyPaidBill();
        return ResponseEntity.ok(list);
    }
    
}
