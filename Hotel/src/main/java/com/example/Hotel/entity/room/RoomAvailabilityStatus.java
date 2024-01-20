package com.example.Hotel.entity.room;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.reservation.Booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class RoomAvailabilityStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    String status;//availbale, booked
    @Column(name = "checkin_date")
    LocalDate checkinDate;
    @Column(name = "checkout_date")
    LocalDate checkoutDate;
    @Column(name = "checkin_time")
    LocalTime checkinTime;
    @Column(name = "checkout_time")
    LocalTime checkoutTime;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    public RoomAvailabilityStatus(Room room, String roomStatus, LocalDate checkInDate, LocalDate checkOutDate, LocalTime checkInTime)
    {
        this.checkinDate = checkInDate;
        this.checkoutDate = checkOutDate;
        this.checkinTime = checkInTime;
        this.checkoutTime = checkInTime;
        this.room = room;
        this.status = roomStatus;
        booking = null;
        customer = null;
    }

}
