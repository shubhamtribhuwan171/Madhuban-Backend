package com.example.Hotel.entity.guest;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GuestDto
{
    @JsonProperty("guestId")
    Long guestId;
    @JsonProperty("title")
    String title;
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("email")
    String email;
    @JsonProperty("phoneNumber")
    Long phoneNumber;
    @JsonProperty("guestDocs")
    Map<String, String> identificationDocs;
    @JsonProperty("bookingId")
    Long bookingId;
    @JsonProperty("age")
    int age;

    public GuestDto(Long guestId, String title, String firstName, String lastName, Map<String, String> map, Long bookingId, int age, String email, Long phoneNumber)
    {
        this.age = age;
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.title = title;
        
        if (this.identificationDocs == null) {
            this.identificationDocs = new HashMap<>();
        }

        if (map != null) {
            this.identificationDocs.putAll(map);
        }
    }
}