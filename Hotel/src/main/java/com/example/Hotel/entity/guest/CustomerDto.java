package com.example.Hotel.entity.guest;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class CustomerDto {
    //@JsonProperty("customerId")
    //Long customerId;
    @JsonProperty("title")
    String title;//Mr, Mrs
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("email")
    String email;//validation in later stage
    @JsonProperty("address")
    String address;
    @JsonProperty("city")
    String city;
    @JsonProperty("state")
    String state;
    @JsonProperty("country")
    String country;
    @JsonProperty("phoneNumber")
    Long phoneNumber;
    @JsonProperty("customerDocs")
    Map<String, String> docId;
    @JsonProperty("age")
    int age;

    public CustomerDto(String firstName, String lastName, String email, String address, String city,
    String state, String country, Long phoneNumber, String title, Map<String, String> map, int age)
    {
        this.age = age;
        this.address = address;
        if (this.docId == null) {
            this.docId = new HashMap<>();
        }

        this.docId.putAll(map);
        //this.customerId = customerId;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.lastName = lastName;
    }
}
