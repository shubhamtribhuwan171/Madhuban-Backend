package com.example.Hotel.service.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.reservation.Billing;
import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.reservation.Transaction;
import com.example.Hotel.entity.reservation.TransactionDto;
import com.example.Hotel.entity.reservation.TransactionResponse;
import com.example.Hotel.repository.reservation.BillingRepository;
import com.example.Hotel.repository.reservation.BookingRepository;
import com.example.Hotel.repository.reservation.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
    
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    BillingRepository billingRepo;
    @Autowired
    BookingRepository bookingRepo;
    @Autowired
    BillingService billService;

    //craete transaction
    @Transactional
    public Long addTransaction(TransactionDto transactionDto)
    {
        Transaction transaction = new Transaction();
        transaction.setAmountPaid(transactionDto.getAmountPaid());
        transaction.setPaymentMode(transactionDto.getPaymentMode());
        transaction.setPaymentDate(transactionDto.getDate());

        /*Long billingId = transactionDto.getBillingId();
        Billing bill = billingRepo.findById(billingId).get();*/
        Long bookingId = transactionDto.getBookingId();
        Booking booking = bookingRepo.findById(bookingId).get();
        Billing bill = billingRepo.findByBooking(booking).get();
        
        transaction.setBilling(bill);
        transactionRepository.save(transaction);
        billingRepo.save(bill);
        return transaction.getTransactionId();
    }
    @Transactional
    //delete transaction
    public void deleteTransaction(Long transactionId, Long bookingId)
    {
        billService.deleteTransactionFromBill(transactionId, transactionId);
        
    }
    //get by date
    public List<TransactionResponse> getTransactionsByDate(LocalDate date)
    {
        List<Transaction> list = transactionRepository.findByPaymentDate(date);
        return mapToList(list);

    }

    //get all transaction by booking id
    public List<TransactionResponse> getTransactionByBilling(Billing bill)
    {
        List<Transaction> list = transactionRepository.findByBilling(bill);
        return mapToList(list);
    }

    //edit transaction
    public void updateTransaction(Long bookingId, Long transactionId, TransactionDto dto) throws Exception
    {
        Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
        if(bookingOpt.isEmpty())
            throw new Exception("Booking Not Found!");
        
        Booking booking = bookingOpt.get();
        Optional<Billing> billOpt = billingRepo.findByBooking(booking);
        if(billOpt.isEmpty())
            throw new Exception("Bill Not Found!");
        
        Billing bill = billOpt.get();
        List<Transaction> list = bill.getTransactions();
        
        int flag = 0;
        for(Transaction t: list)
        {
            if(t.getTransactionId().equals(transactionId))
            {
                t.setPaymentMode(dto.getPaymentMode());
                t.setAmountPaid(dto.getAmountPaid());
                t.setPaymentDate(dto.getDate());
                transactionRepository.save(t);
                flag = 1;
            }
        }
        if(flag == 0)
            throw new Exception("Transaction Not Found!");

    }

    TransactionResponse mapToDto(Transaction transactionObj)
    {
        Long transactionId = transactionObj.getTransactionId();
        Double amountPaid = transactionObj.getAmountPaid();
        String paymentMode = transactionObj.getPaymentMode();
        LocalDate date = transactionObj.getPaymentDate();
        Long billingId = transactionObj.getBilling().getBillingId();
        Long bookingId = transactionObj.getBilling().getBooking().getBookingId();
        TransactionResponse response = new TransactionResponse();
        response.setAmountPaid(amountPaid);
        response.setBookingId(bookingId);
        response.setBillId(billingId);
        response.setPaymentDate(date);
        response.setPaymentMode(paymentMode);
        response.setTranasctionId(transactionId);

        return response;
    }

    List<TransactionResponse> mapToList(List<Transaction> transactionList)
    {
        return transactionList.stream()
            .map(transaction -> mapToDto(transaction))
            .collect(Collectors.toList());

    }
    
}
