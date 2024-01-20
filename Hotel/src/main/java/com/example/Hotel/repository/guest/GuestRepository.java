package com.example.Hotel.repository.guest;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.guest.Guest;
import com.example.Hotel.entity.reservation.Booking;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{
    List<Guest> findByBooking(Booking booking);
}
