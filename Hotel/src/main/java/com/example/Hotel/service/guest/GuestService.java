package com.example.Hotel.service.guest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.guest.Guest;
import com.example.Hotel.entity.guest.GuestDto;
import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.repository.guest.GuestRepository;
import com.example.Hotel.repository.reservation.BookingRepository;

@Service
public class GuestService {

    @Autowired
    GuestRepository guestRepository;
    @Autowired
    BookingRepository bookingRepository;

    //add guest
    public String addGuest(GuestDto guestDto)
    {
        Guest guest = mapToObj(guestDto);
        Booking b = guest.getBooking();
        b.addGuest(guest);
        guestRepository.save(guest);
        bookingRepository.save(b);
        return guest.getBooking().getBookingId()+"";
    }

    //update customer details
    public void updateGuestDetails(GuestDto guestDto, Long guestId) throws Exception
    {
        
        Optional<Guest> guestOptional = guestRepository.findById(guestId);
        try
        {
            Guest guest = guestOptional.get();
        guest.setAge(guestDto.getAge());
        
        guest.setIdentificationDocs(guestDto.getIdentificationDocs());
        guest.setEmail(guestDto.getEmail());
        guest.setFirstName(guestDto.getFirstName());
        guest.setLastName(guestDto.getLastName());
        guest.setTitle(guestDto.getTitle());
        guest.setPhoneNumber(guestDto.getPhoneNumber());
        guestRepository.save(guest);
        }
        catch(Exception e)
        {
        throw new Exception("Guest Not found");
        }
    }

    //add guest list
    public Long addGuestList(List<GuestDto> list) throws Exception
    {
        if(list.size() == 0)
            return null;
        Long id = list.get(0).getBookingId();
        Optional<Booking> optional = bookingRepository.findById(id);
        if(optional.isPresent())
        {
            Booking b = optional.get();
            for(GuestDto obj: list)
            {
                guestRepository.save(mapToObj(obj));
            
                b.addGuest(mapToObj(obj));
                //bookingRepository.save(b);
            }
            bookingRepository.save(b);
            return list.get(0).getBookingId();
        }
        else
        {
            throw new Exception("booking id not found");
        }
    }

    //get all guest
    public List<GuestDto> getAllGuest()
    {
        List<Guest> list =  guestRepository.findAll();
        return mapListToDTO(list);
    }

    //get by booking id
    public List<GuestDto> getByBookingId(Long bookingId)
    {
        Booking booking = bookingRepository.findById(bookingId).get();
        List<Guest> list = guestRepository.findByBooking(booking);
        return mapListToDTO(list);
    }


    public Guest mapToObj(GuestDto guestDto) {
        //Long guestId = guestDto.getGuestId();
        String firstName = guestDto.getFirstName();
        String lastName = guestDto.getLastName();
        Long bookingId = guestDto.getBookingId();
        
        String email = guestDto.getEmail();
        String title = guestDto.getTitle();

        Long phoneNumber = guestDto.getPhoneNumber();
        int age = guestDto.getAge();
        Booking booking = bookingRepository.findById(bookingId).get();
        Map<String, String> guestDocs = guestDto.getIdentificationDocs();
        Guest guest = new Guest(title, firstName, lastName, phoneNumber, age, guestDocs, booking, email);
        return guest;
    }

    /*private static List<Guest> mapToObjList(List<GuestDto> guestList)
    {
        return guestList.stream()
            .map(GuestService :: mapToObj)
            .collect(Collectors.toList());
    }*/

    //map object to dto
    public GuestDto mapToDTO(Guest guest) {
        String firstName = guest.getFirstName();
        String lastName = guest.getLastName();
        Long guestId = guest.getGuestId();
        Map<String, String> identificationDocs = guest.getIdentificationDocs();
        Booking booking = guest.getBooking();
        int age = guest.getAge();
        String title = guest.getTitle();
        String email = guest.getEmail();
        Long phoneNumber = guest.getPhoneNumber();
        GuestDto guestDto = new GuestDto(guestId,title , firstName, lastName, identificationDocs, booking.getBookingId(), age, email, phoneNumber);
    
        return guestDto;
    }

    // If needed, you can also have a method to convert a list of entities to DTOs
    public List<GuestDto> mapListToDTO(List<Guest> guests) {
        return guests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    

}
