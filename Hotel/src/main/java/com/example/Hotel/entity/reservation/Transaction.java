package com.example.Hotel.entity.reservation;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    Long transactionId;

    Double amountPaid;
    String paymentMode;
    LocalDate paymentDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_id")  // Assuming 'billing_id' is the foreign key column in Transaction referencing Billing
    private Billing billing;

}
