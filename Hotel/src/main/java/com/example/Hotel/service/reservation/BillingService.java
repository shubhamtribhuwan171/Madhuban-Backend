package com.example.Hotel.service.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.reservation.Billing;
import com.example.Hotel.entity.reservation.BillingDto;
import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.reservation.Transaction;
import com.example.Hotel.entity.reservation.TransactionResponse;
import com.example.Hotel.repository.reservation.BillingRepository;
import com.example.Hotel.repository.reservation.BookingRepository;
import com.example.Hotel.repository.reservation.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BillingService {
    
    @Autowired
    TransactionRepository transactionRepo;
    @Autowired
    BillingRepository billingRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    BookingRepository bookingRepo;

    //add bill
    @Transactional
    public Long createBill(Long bookingId) throws Exception
    {
        Optional<Booking> bookingOptional = bookingRepo.findById(bookingId);
        if(!bookingOptional.isPresent())
        {
            throw new Exception("booking not found");
        }
        else{
        Booking booking = bookingOptional.get();
        Billing bill = new Billing(booking);
        
        LocalDate checkIn = booking.getCheckin();
        LocalDate checkOut = booking.getCheckout();
        //long durationDays = ChronoUnit.DAYS.between(checkIn, checkOut);
        bill.setCheckinDate(checkIn);
        bill.setCheckoutDate(checkOut);
        bill.setCheckinTime(booking.getCheckinTime());
        bill.setCheckoutTime(booking.getCheckoutTime());
        Double total = booking.getTotalAmount();
        Double addOnCost = booking.getAddOnTotal();

        bill.setTransactions(null);
        bill.setPaidAmount(0d);
        bill.setTotalAddOnCharges(addOnCost);
        bill.setTotalRoomCharges(total);
        bill.setPendingAmount(total+addOnCost);
        billingRepository.save(bill);
        return bill.getBillingId();
        }
    }

    //get the bill by booking id
    public BillingDto getBill(Long bookingId) throws Exception
    {
    
        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if(!bookingOpt.isPresent())
        {
            throw new Exception("Booking Not Found");
        }
        Booking booking = bookingOpt.get();
        Optional<Billing> opt = billingRepository.findByBooking(booking);
        if(!opt.isPresent())
        {
            throw new Exception("Bill not found");
        }


        return mapToDto(opt.get());
    }
    
    //add transaction to list of transactions
    @Transactional
    public void addTransactionToBill(Long transactionId, Long bookingId) throws Exception
    {
        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if(!bookingOpt.isPresent())
        {
            throw new Exception("Booking Not Found");
        }
        Booking booking = bookingOpt.get();
        Optional<Billing> opt = billingRepository.findByBooking(booking);
        if(!opt.isPresent())
        {
            throw new Exception("Bill not found");
        }
        Billing bill = opt.get();
        Transaction t = transactionRepo.findById(transactionId).get();
        
        bill.addTransacations(t);
        Double amountPaid = t.getAmountPaid();
        bill.setPaidAmount(bill.getPaidAmount()+amountPaid);//total paid amount: already stored + paid amount from new transaction
        Double total = (bill.getTotalAddOnCharges() + bill.getTotalRoomCharges()) - bill.getPaidAmount();
        bill.setPendingAmount(total);
        billingRepository.save(bill);
        transactionRepo.save(t);
    }
    
    //delete bill
    @Transactional
    public void deleteBill(Long bookingId) throws Exception
    {
        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if(!bookingOpt.isPresent())
        {
            throw new Exception("Booking Not Found");
        }
        Booking booking = bookingOpt.get();
        billingRepository.deleteByBooking(booking);
    }

    //get all transactions of booking id
    public List<TransactionResponse> getTransactions(Long bookingId) throws Exception
    {
        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if(!bookingOpt.isPresent())
        {
            throw new Exception("Booking Not Found");
        }
        Booking booking = bookingOpt.get();
        Optional<Billing> opt = billingRepository.findByBooking(booking);
        if(!opt.isPresent())
        {
            throw new Exception("Bill not found");
        }
        Billing bill = opt.get();
        List<TransactionResponse> response = transactionService.getTransactionByBilling(bill);
        return response;
    }

    //delete transaction from bill
    // Delete transaction from bill
    public void deleteTransactionFromBill(Long transactionId, Long bookingId) {
    Booking booking = bookingRepo.findById(bookingId).get();

    if (booking != null) {
        Billing bill = billingRepository.findByBooking(booking).get();

        if (bill != null) {
            List<Transaction> transactions = bill.getTransactions();
            Transaction transactionToRemove = transactionRepo.findByTransactionId(transactionId).get();

            if (transactionToRemove != null) {
                transactions.remove(transactionToRemove);
                transactionRepo.delete(transactionToRemove);
                billingRepository.save(bill);
            } else {
                // Handle case when the transaction with given ID is not found
                throw new IllegalArgumentException("Transaction with ID " + transactionId + " not found.");
            }
        } else {
            // Handle case when billing information is not found for the booking
            throw new IllegalArgumentException("Billing information not found for booking with ID " + bookingId);
        }
    } else {
        // Handle case when the booking with given ID is not found
        throw new IllegalArgumentException("Booking with ID " + bookingId + " not found.");
    }
}

    
    //get all bills with 0 pending amount
    public List<BillingDto> getFullyPaidBill()
    {
        List<Billing> bills =  billingRepository.findByPendingAmount(0l);
        return mapToList(bills);
    }

    BillingDto mapToDto(Billing billing)
    {
        Long billId = billing.getBillingId();
        Long bookingId = billing.getBooking().getBookingId();
        LocalDate checkin = billing.getCheckinDate();
        LocalDate checkout = billing.getCheckoutDate();
        LocalTime checkinTime = billing.getCheckinTime();
        LocalTime checkoutTime = billing.getCheckoutTime();
        Double totalRoomCharges = billing.getTotalRoomCharges();
        Double totalAddOnCHarges = billing.getTotalAddOnCharges();
        Double pendingAmount = billing.getPendingAmount();
        Double paidAmount = billing.getPaidAmount();
        List<Transaction> list = billing.getTransactions();
        List<String> tList = new ArrayList<>();
        for(Transaction t : list)
        {
            String s = t.getTransactionId() + " "+ t.getAmountPaid() + " " + t.getPaymentMode() + " "+ t.getPaymentDate();
            tList.add(s);
        }

        BillingDto response = new BillingDto();
        response.setPaidAmount(paidAmount);
        response.setBookingId(bookingId);
        response.setBillingId(billId);
        response.setPendingAmount(pendingAmount);;
        response.setTransactions(tList);
        response.setCheckinDate(checkin);
        response.setCheckinTime(checkinTime);
        response.setCheckoutDate(checkout);
        response.setCheckoutTime(checkoutTime);
        response.setTotalAddOnCharges(totalAddOnCHarges);
        response.setTotalRoomCharges(totalRoomCharges);
    

        return response;
    }

    List<BillingDto> mapToList(List<Billing> billList)
    {
        return billList.stream()
            .map(billing -> mapToDto(billing))
            .collect(Collectors.toList());

    }
}
