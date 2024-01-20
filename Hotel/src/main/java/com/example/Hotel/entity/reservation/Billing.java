package com.example.Hotel.entity.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Billing {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "billing_id")
    Long billingId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    Booking booking;

    @Column(name = "total_room_charges")
    Double totalRoomCharges;
    @Column(name = "total_addon_charges")
    Double totalAddOnCharges;
    @Column(name = "paid_amount")
    Double paidAmount;
    @Column(name = "pending_amount")
    Double pendingAmount;
    @Column(name = "checkin_date")
    LocalDate checkinDate;
    @Column(name = "checkout_date")
    LocalDate checkoutDate;

    @Column(name = "checkin_time")
    LocalTime checkinTime;
    @Column(name = "checkout_time")
    LocalTime checkoutTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billing")
    @Column(name = "transaction_list")
    @JsonIgnore
    List<Transaction> transactions;

    public Billing(Booking booking)
    {
        this.booking = booking;
        transactions = new ArrayList<>();
    }


    public void addTransacations(Transaction t)
    {
        transactions.add(t);
    }
}
