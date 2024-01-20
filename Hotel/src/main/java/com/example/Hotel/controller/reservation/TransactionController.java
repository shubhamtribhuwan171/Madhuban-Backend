package com.example.Hotel.controller.reservation;

import java.time.LocalDate;
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

import com.example.Hotel.entity.reservation.TransactionDto;
import com.example.Hotel.entity.reservation.TransactionResponse;
import com.example.Hotel.service.reservation.BillingService;
import com.example.Hotel.service.reservation.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    
    @Autowired
    TransactionService transactionService;
    @Autowired
    BillingService billService;

    //add transaction
    @PutMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDto transactionDto)
    {
        try{
        Long transactionId = transactionService.addTransaction(transactionDto);
        Long bookingId = transactionDto.getBookingId();
        billService.addTransactionToBill(transactionId, bookingId);
        return ResponseEntity.ok("Transaction added!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error craeting transaction: " + e.getMessage());
        }

    }
    //delete transaction
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTransaction(@RequestParam Long transactionId, @RequestParam Long bookingId)
    {
        try{
        transactionService.deleteTransaction(transactionId, bookingId);
        return ResponseEntity.ok("Transaction deleted!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting transaction: " + e.getMessage());
        }
    }
    //get transactions by date
    @GetMapping("/getByDate")
    public ResponseEntity<List<TransactionResponse>> getByDate(@RequestParam(required = false) LocalDate date)
    {
        if(date == null)
            date = LocalDate.now();
        List<TransactionResponse> list = transactionService.getTransactionsByDate(date);
        return ResponseEntity.ok(list);
    }

    /* access this method from billingservice
    get by booking id
    @GetMapping("/getByBooking")
    public ResponseEntity<List<TransactionResponse>> getByBooking(@RequestParam Long bookingId)
    {
        List<TransactionResponse> list = transactionService.getTransactionByBilling(null)

    }*/

    @PutMapping("/edit")
    public ResponseEntity<?> updateTransaction(@RequestParam Long bookingId, @RequestParam Long transactionId, @RequestBody TransactionDto dto)
    {
        try{
            transactionService.updateTransaction(bookingId, transactionId, dto);
        return ResponseEntity.ok("Tranasction Updated!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating transaction: " + e.getMessage());
        }
    }
    

}
