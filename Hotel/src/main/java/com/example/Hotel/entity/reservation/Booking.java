package com.example.Hotel.entity.reservation;

//import java.util.HashMap;
//import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.Cascade;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.guest.Guest;
import com.example.Hotel.entity.room.AddOns;
import com.example.Hotel.entity.room.Room;



import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    //@SequenceGenerator(name = "booking_seq", sequenceName = "booking_sequence", initialValue = 1001)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @PreRemove
    private void preRemove() {
        // Set the room status to 'vacant' when a booking is deleted
        if (room != null) {
            room.setRoomStatus("vacant");
        }
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    int totalGuestCount;

    LocalDate checkin;
    LocalDate checkout;

    LocalTime checkinTime;
    LocalTime checkoutTime;

    //map to store addon with quantity
    @ElementCollection
    @CollectionTable(name = "addon_qty_map", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyJoinColumn(name = "addon_id")
    @Column(name = "quantity")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    Map<AddOns, Integer> addOnMap = new HashMap<>();
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guest> guests = new ArrayList<>();
    
    String paymentStatus;

    Double addOnTotal;
    Double totalAmount;
    Double pendingAmount;
    Double paidAmount;

    //Map<String, Integer> addOnQty = new HashMap<>();
    String modeOfPayment;

    public Booking()
    {
        
    }
    //booking with add ons
    public Booking(Room room, Customer customer, int totalGuestCount, LocalDate checkInF, LocalDate checkOutF,
    LocalTime checkIntime, LocalTime checkOutTime, Double totalAmount, Double paidAmt, AddOns addOn, Integer qty) {
        this.room = room;
        this.customer = customer;
        this.totalGuestCount = totalGuestCount;
        this.checkin = checkInF;
        this.checkout = checkOutF;
        
        this.checkinTime = checkIntime;
        this.checkoutTime = checkOutTime;

        addOnMap.put(addOn, qty);
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmt;
        calculatePending(paidAmt);
    }

    //booking without add on
    public Booking(Room room, Customer customer, int totalGuestCount, LocalDate checkInF, LocalDate checkOutF,
    LocalTime checkInTime, LocalTime checkOutTime,  Double totalAmount, Double paidAmt) {
        this.room = room;
        this.customer = customer;
        this.totalGuestCount = totalGuestCount;
        this.checkin = checkInF;
        this.checkout = checkOutF;
        this.checkinTime = checkInTime;

        this.checkoutTime = checkOutTime;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmt;
        calculatePending(paidAmt);
    }

    void calculatePending(Double paidAmount)
    {
        this.pendingAmount = totalAmount - paidAmount;
        if(pendingAmount == 0)
            setPaymentStatus("Paid");
        else if(pendingAmount > 0 && pendingAmount < totalAmount)
            setPaymentStatus("Partially Paid");
        else
            setPaymentStatus("Pending");
    }

    public void setPaymentStatus(String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    
    public void setPaidAmount(Double paid)
    {
        this.paidAmount = paid;
        calculatePending(paid);
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
        guest.setBooking(this); // Set the reference in the Guest entity
    }

    public void removeGuest(Guest guest) {
        guests.remove(guest);
        guest.setBooking(null); // Remove the reference in the Guest entity
    }
    
}
