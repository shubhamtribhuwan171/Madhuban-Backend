package com.example.Hotel.repository.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.guest.Customer;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByPhoneNumber(Long phoneNumber);
    List<Customer> findByCity(String city);
    List<Customer> findByState(String state);
    List<Customer> findByCountry(String country);
    void deleteByCustomerId(Long custId);
    Optional<Customer> findByCustomerId(Long custId);
}
