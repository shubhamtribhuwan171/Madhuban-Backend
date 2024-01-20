package com.example.Hotel.entity.guest;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.Cascade;

import com.example.Hotel.entity.reservation.Booking;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Guest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    Long guestId;
    @Column(name = "title")
    String title;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "email")
    String email;
    @Column(name = "phone_number")
    Long phoneNumber;
    @Column(name = "age")
    int age;
    
    @ElementCollection
    @CollectionTable(name = "guest_docs", joinColumns = @JoinColumn(name = "guestId"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    Map<String, String> identificationDocs;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public Guest()
    {
        this.identificationDocs = new HashMap<>();
    }

    public Guest(String title, String firstName, String lastName, Long phoneNumber, int age, Map<String, String> idMap, Booking booking, String email) {
        this.firstName = firstName;
       // this.guestId = guestId;
        this.lastName = lastName;
        this.booking = booking;

        this.phoneNumber = phoneNumber;
        this.email = email;
        this.title = title;
        this.age = age;
        if (this.identificationDocs == null) {
            this.identificationDocs = new HashMap<>();
        }
    
        if (idMap != null) {
            this.identificationDocs.putAll(idMap);
        }
        
    }
    
/*    public void setMapContent(Map<String, String> map)
    {
        for(Map.Entry<String, String> entry: map.entrySet())
        {
            this.identificationDocs.put(entry.getKey(), entry.getValue());
        }
    }
*/
    public void addGuestDocs(Map<String, String> docMap)//if already not present then add details
    {
        for(Map.Entry<String, String> entry: docMap.entrySet())
        {
            if(!this.identificationDocs.containsKey(entry.getKey()))
            {
                this.identificationDocs.put(entry.getKey(), entry.getValue());
            }
        }
    }

}
