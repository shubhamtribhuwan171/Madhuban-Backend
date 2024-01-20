package com.example.Hotel.repository.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.reservation.Billing;
import com.example.Hotel.entity.reservation.Booking;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long>{
    Optional<Billing> findByBooking(Booking booking);
    void deleteByBooking(Booking booking);
    //@Query("select b from billing b where b.pendingAmount = 0")
    List<Billing> findByPendingAmount(Long amount);
}
