package com.example.Hotel.repository.reservation;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.reservation.Billing;
import com.example.Hotel.entity.reservation.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    Optional<Transaction> findByTransactionId(Long transactionId);
    void deleteByTransactionId(Long tansactionId);
    List<Transaction> findByPaymentDate(LocalDate date);
    List<Transaction> findByBilling(Billing billing);
}
