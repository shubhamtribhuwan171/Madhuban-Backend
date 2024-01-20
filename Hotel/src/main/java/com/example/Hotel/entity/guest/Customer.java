package com.example.Hotel.entity.guest;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.Cascade;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long customerId;
    @Column(name = "title")
    String title;//Mr, Mrs
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "email")
    String email;//validation in later stage
    @Column(name = "address")
    String address;
    @Column(name = "city")
    String city;
    @Column(name = "state")
    String state;
    @Column(name = "country")
    String country;
    @Column(name = "phone_number")
    Long phoneNumber;
    @Column(name ="age")
    int age;
    
    @ElementCollection
    @CollectionTable(name = "customer_docs", joinColumns = @JoinColumn(name = "customerId"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL) // Use Hibernate-specific cascade
    Map<String,String> customerDocs;

    //Photos of Id proof can be uploaded and stored locally

    public Customer()
    {

    }

    public Customer(String firstName, String lastName, String email, String address, String city, String state, String country,
        Long phoneNumber, String title, Map<String, String> idMap, int age)
    {
        if (this.customerDocs == null) {
            this.customerDocs = new HashMap<>();
        }

        this.customerDocs.putAll(idMap);
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
        this.age = age;
    }

/*     public void setMapContent(Map<String, String> map)
    {
        for(Map.Entry<String, String> entry: map.entrySet())
        {
            this.customerDocs.put(entry.getKey(), entry.getValue());
        }
    }
*/

    public void addCustomerDocs(String name, String docId)//if already not present then add details
    {
            this.customerDocs.put(name, docId);
        
    }

    public void deleteCustomerDocs(String name)
    {
        if(this.customerDocs.containsKey(name))
            customerDocs.remove(name);
    }
}
