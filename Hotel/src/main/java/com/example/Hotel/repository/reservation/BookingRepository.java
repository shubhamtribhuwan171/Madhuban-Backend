package com.example.Hotel.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.room.Room;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByCheckin(LocalDate checkin);
    List<Booking> findByCustomer(Customer customer);
    Optional<Booking> findByBookingId(Long bookingId);
    void deleteByBookingId(Long bookingId);
    Optional<Booking> findByRoom(Room room);
    //Optional<Booking> findByBooking(Booking booking);
}
