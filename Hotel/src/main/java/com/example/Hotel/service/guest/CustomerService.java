package com.example.Hotel.service.guest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.guest.CustomerDto;
import com.example.Hotel.repository.guest.CustomerRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class CustomerService {
    
    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    //add customer
    public Long addCustomer(CustomerDto customerDto)
    {
        Customer customer = mapToObj(customerDto);
        customerRepository.save(customer);
        return customer.getCustomerId();
    }


    @Transactional
    //update customer details
    public void updateCustomerDetails(CustomerDto custDto, Long customerId) throws Exception
    {
        Optional<Customer> custOptional = customerRepository.findById(customerId);
        if(custOptional.isPresent())
        {
            Customer cust = custOptional.get();
            cust.setAddress(custDto.getAddress());
            cust.setAge(custDto.getAge());
            cust.setCity(custDto.getCity());
            cust.setState(custDto.getState());
            cust.setCountry(custDto.getCountry());
            cust.setCustomerDocs(custDto.getDocId());
            cust.setEmail(custDto.getEmail());
            cust.setFirstName(custDto.getFirstName());
            cust.setLastName(custDto.getLastName());
            cust.setTitle(custDto.getTitle());
            cust.setPhoneNumber(custDto.getPhoneNumber());
            customerRepository.save(cust);
        }
        else
            throw new Exception("Customer Not found");

    }

    //get all customer
    public List<CustomerDto> getAllCustomers()
    {
        List<Customer> list =  customerRepository.findAll();
        return mapListToDTO(list);
    }

    
    //get customers by phone number(unique)
    public CustomerDto getByPhoneNumber(Long phone) throws Exception
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phone);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            return mapToDTO(cust);
        }
        else
            throw new Exception("Customer Not found");
    }

    //get by customer id
    public Customer getByCustomerId(Long custId) throws Exception
    {
        
        Optional<Customer> opt =  customerRepository.findById(custId);
        if(opt.isPresent())
        {
            return opt.get();
        }
        else
            throw new Exception("Customer Not found");
    }

    //add document ids
    public void addDocDetails(Map<String, String> map, Long phoneNumber) throws Exception
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phoneNumber);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            for(Map.Entry<String, String> entry: map.entrySet())
            {
                cust.addCustomerDocs(entry.getKey(), entry.getValue());
            }
            customerRepository.save(cust);
        }
        else
            throw new Exception("Customer adding docs");
    }
    
    //update doc ids
    public void updateDocDetails(String docName, String docId, Long phoneNumber) throws Exception
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phoneNumber);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            for(Map.Entry<String, String> entry: cust.getCustomerDocs().entrySet())
            {
                if(entry.getKey().equals(docName))
                {
                    entry.setValue(docId);
                }
            }
            customerRepository.save(cust);
        }
        else
            throw new Exception("Customer updating docs");
    }

    //delete by cust id
    public String deleteCust(Long custId) throws Exception
    {
        Optional<Customer> optional = customerRepository.findByCustomerId(custId);
        if(optional.isPresent())
        {
            customerRepository.deleteById(custId);
            return "Cutomer Deleted Successfully!";
        }
        else
        {
            throw new Exception("Customer Not Found!");
        }
    }

    private static Customer mapToObj(CustomerDto customerDto)
    {
        String title = customerDto.getTitle();//Mr, Mrs
        String firstName = customerDto.getFirstName();
        String lastName = customerDto.getLastName();
        String email = customerDto.getEmail();//validation in later stage
        String address = customerDto.getAddress();
        String city = customerDto.getCity();
        String state = customerDto.getState();
        //Long customerId = customerDto.getCustomerId();
        String country = customerDto.getCountry();
        Long phoneNumber = customerDto.getPhoneNumber();
        Map<String, String> map = customerDto.getDocId();
        int age = customerDto.getAge();

        Customer customer = new Customer(firstName, lastName, email, address, city, state, country, phoneNumber, title, map, age);
        return customer;
    }

    
    //map object to dto
    private static CustomerDto mapToDTO(Customer customer) {
        String title = customer.getTitle();//Mr, Mrs
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String email = customer.getEmail();//validation in later stage
        String address = customer.getAddress();
        String city = customer.getCity();
        String state = customer.getState();
        //Long customerId = customer.getCustomerId();
        String country = customer.getCountry();
        Long phoneNumber = customer.getPhoneNumber();
        Map<String, String> map = customer.getCustomerDocs();
        int age = customer.getAge();
        CustomerDto customerDto = new CustomerDto(
            firstName,lastName,email,address,city,state,country, phoneNumber,title, map, age);

        return customerDto;
    }

    // If needed, you can also have a method to convert a list of entities to DTOs
    private static List<CustomerDto> mapListToDTO(List<Customer> customers) {
        return customers.stream()
                .map(CustomerService::mapToDTO)
                .collect(Collectors.toList());
    }
}

/*private static List<Customer> mapToObjList(List<CustomerDto> customerList)
    {
        return customerList.stream()
            .map(CustomerService :: mapToObj)
            .collect(Collectors.toList());
    }*/

    
/* 
    //update  city
    public void updateCity(String newCity, Long phoneNumber)
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phoneNumber);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            cust.setCity(newCity);
            customerRepository.save(cust);
        }
    }
    
    //update country
    public void updateCountry(String country, Long phoneNumber)
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phoneNumber);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            cust.setCountry(country);
            customerRepository.save(cust);
        }
    }
    
    //update title
    public void updateTitle(String newTitle, Long phoneNumber)
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phoneNumber);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            cust.setTitle(newTitle);
            customerRepository.save(cust);
        }
    }
    
    //update name(both first and last)
    public void updateAddress(String newFirst, String newLast, Long phoneNumber)
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phoneNumber);
        if(optional.isPresent())
        {
            Customer cust = optional.get();
            cust.setFirstName(newFirst);
            cust.setLastName(newLast);
            customerRepository.save(cust);
        }
    }*/