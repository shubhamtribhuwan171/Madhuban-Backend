package com.example.Hotel.controller.reservation;

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

import com.example.Hotel.entity.reservation.BookingDto;
import com.example.Hotel.entity.reservation.BookingSummaryDto;
import com.example.Hotel.entity.reservation.NestedResponse;
import com.example.Hotel.service.guest.CustomerService;
import com.example.Hotel.service.reservation.BillingService;
import com.example.Hotel.service.reservation.BookingService;
import com.example.Hotel.service.room.RoomService;
import com.example.Hotel.service.room.RoomStatusService;

@RestController
@RequestMapping("/reservation")
public class BookingController {

    @Autowired
    BookingService bookingService;
    @Autowired
    RoomStatusService statusService;
    @Autowired
    RoomService roomService;
    @Autowired
    BillingService billingService;
    @Autowired
    CustomerService customerService;

    @PostMapping("/addNested")
    public ResponseEntity<?> addAll(@RequestBody NestedResponse nested) throws Exception
    {
        
            Long bookingId = bookingService.addAll(nested);
            if(bookingId != null)
            {
                billingService.createBill(bookingId);
                return ResponseEntity.ok(bookingId);
            }

        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Room Not Available for these dates!");
        }
        

    }
    

    @GetMapping("/getAll")
    public ResponseEntity<List<BookingSummaryDto>> getAllBookings()
    {
        List<BookingSummaryDto> list = bookingService.getAll();
        return ResponseEntity.ok(list);
    }
    
    
    @GetMapping("/getByCheckIn")
    public ResponseEntity<List<BookingDto>> getByCheckInDate(@RequestParam String dateString)
    {
        List<BookingDto> list = bookingService.getByCheckInDate(dateString);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getByCustomer")
    public ResponseEntity<List<BookingDto>> getByCustomer(@RequestParam Long phoneNumber)
    {
        List<BookingDto> list = bookingService.getByCustomer(phoneNumber);
        return ResponseEntity.ok(list);
    }

    

    @GetMapping("/getSummary")
    public ResponseEntity<?> getBookingSummary(@RequestParam Long bookingId)
    {
        try
        {
        BookingSummaryDto obj = bookingService.getBookingSummary(bookingId);
        return ResponseEntity.ok(obj);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting summary: " + e.getMessage());
        }
    }

        
    //delete by booking id
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByBookingId(@RequestParam Long bookingId)
    {
        try{
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok("Booking deleted!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting booking: " + e.getMessage());
        }
    }

    @PutMapping("/addPaymentDetails")
    public ResponseEntity<?> addPayment(@RequestParam Long bookingId, @RequestParam String paymentMode, @RequestParam Double paidAmount)
    {
        try
        {
            
            bookingService.addPayment(bookingId, paymentMode, paidAmount);
                return ResponseEntity.ok("Payment Details Updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating payment details: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getBookingById(@RequestParam Long id)
    {
            BookingDto obj = bookingService.getByBookingId(id);
            return ResponseEntity.ok(obj);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBooking(@RequestParam Long bookingId,@RequestBody BookingDto bookingDto)
    {
        try
        {
            Long response = bookingService.updateBooking(bookingId, bookingDto);
            //Long bookingId = Long.parseLong(response);
            if(response == null)
            {
                throw new Exception("Customer Or Booking Not Found!");
            }
            billingService.createBill(bookingId);
                return ResponseEntity.ok(""+ response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating booking: " + e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editBooking(@RequestBody Long bookingId, @RequestBody NestedResponse nested, @RequestParam String dateString) throws Exception
    {
        
         try{
            bookingId = bookingService.editBooking(bookingId, nested, dateString);
            
            return ResponseEntity.ok("Booking Updated!");
         }  
         catch(Exception e)
         { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Room Not Available for these dates!");
        }
        

    }

}



/*get next booking id
    @PostMapping("/add")
    public ResponseEntity<?> getNext()
    {
        Long bookingId = bookingService.addBooking();
        return ResponseEntity.ok(bookingId+"");
    }*/

        /*@PutMapping("/add")
    public ResponseEntity<?> addBooking(@RequestBody BookingDto bookingDto)
    {
        /*try
        {
            String response = bookingService.addBooking(bookingDto);
            Long bookingId = Long.parseLong(response);
            billingService.createBill(bookingId);
                return ResponseEntity.ok(""+ response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error booking room: " + e.getMessage());
        }

    }*/

    

