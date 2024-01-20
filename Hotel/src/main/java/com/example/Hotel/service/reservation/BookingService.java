package com.example.Hotel.service.reservation;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.guest.CustomerDto;
import com.example.Hotel.entity.guest.Guest;
import com.example.Hotel.entity.guest.GuestDto;
import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.reservation.BookingDto;
import com.example.Hotel.entity.reservation.BookingSummaryDto;
import com.example.Hotel.entity.reservation.NestedResponse;
import com.example.Hotel.entity.room.AddOns;
import com.example.Hotel.entity.room.Room;
import com.example.Hotel.entity.room.RoomConfig;
import com.example.Hotel.repository.guest.CustomerRepository;
import com.example.Hotel.repository.guest.GuestRepository;
import com.example.Hotel.repository.reservation.BookingRepository;
import com.example.Hotel.repository.room.AddOnRepository;
import com.example.Hotel.repository.room.RoomAvailabilityStatusRepository;
import com.example.Hotel.repository.room.RoomConfigRepository;
import com.example.Hotel.repository.room.RoomRepository;
import com.example.Hotel.service.guest.CustomerService;
import com.example.Hotel.service.guest.GuestService;
import com.example.Hotel.service.room.RoomStatusService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    AddOnRepository addOnRepo;
    
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    CustomerService customerService;

    @Autowired
    RoomConfigRepository configRepo;

    @Autowired
    RoomStatusService statusService;

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    GuestService guestService;

    @Autowired
    RoomAvailabilityStatusRepository statusRepo;

    @PersistenceContext
    private EntityManager entityManager;


    //get by booking id
    public BookingDto getByBookingId(Long id)
    {
        Booking booking = bookingRepository.findById(id).get();
        return mapToDto(booking);

    }

    @Transactional
    //add nested
    public Long addAll(NestedResponse obj) throws Exception
    {
        CustomerDto customerDto = obj.getCust();
        Long custId = customerService.addCustomer(customerDto);
        //Long bookingId = addBooking();

        BookingDto bookingDto = obj.getBooking();
        
        LocalDate checkIn = LocalDate.parse(bookingDto.getCheckIn());
        LocalDate checkOut = LocalDate.parse(bookingDto.getCheckOut());
        boolean ifBooked = ifAlreadyBooked(bookingDto.getRoomNumber(), checkIn, checkOut);

        if(!ifBooked)
        {
            bookingDto.setCustId(custId);

        
            logger.info("Adding booking...");
    //Booking booking = updateBooking(bookingDto);
    
            Booking booking = updateBooking(bookingDto);
            logger.info("Booking added: {}", booking.getBookingId());
            Long bookingId = booking.getBookingId();
            List<GuestDto> guests = obj.getGuests();
            if(guests != null)
            {
                for(GuestDto g:guests)
                {
                    g.setBookingId(bookingId);
                }
                guestService.addGuestList(guests);
            }
            //Booking b = mapToObj(bookingDto);
            //bookingRepository.save(b);
            return bookingId;
        }
        else
        {
            return null;
        }
    }

    //validation
    public boolean ifAlreadyBooked(Integer roomNumber, LocalDate checkIn, LocalDate checkOut) {
        return statusRepo.existsByRoomNumberAndOverlappingDates(roomNumber, checkIn, checkOut);
    }
   /* public boolean ifAlreadyBooked(Integer roomNumber, LocalDate checkIn, LocalDate checkOut)
    {
        Room room = roomRepo.findByRoomNumber(roomNumber).get();


        RoomAvailabilityStatus obj = s
        //List<RoomAvailabilityStatus> objList = list.stream().map(this::mapToDto).collect(Collectors.toList());

        if(obj == null)
            return false;
        
        else
            return true;
        
    }*/

    
    //add booking
    @Transactional
    public Booking updateBooking(BookingDto bookingDto) throws Exception
    {
        logger.info("Updating booking...");
    
        //Booking booking = bookingRepository.findByBookingId(bookingId).get();
        Room room = roomRepo.findByRoomNumber(bookingDto.getRoomNumber()).get();

            Optional<Customer> custOpt = customerRepository.findById(bookingDto.getCustId());
            if(custOpt.isEmpty())
            {
                return null;
            }
            Customer cust = custOpt.get();
            int guestCnt = bookingDto.getTotalGuestCount();
        
            Map<String, Integer> addOnMap = bookingDto.getAddOnMap();
            Map<AddOns, Integer> addOn = new HashMap<>();
            Double addOnTotal = 0d;
            for(Map.Entry<String, Integer> entry: addOnMap.entrySet())
            {
                String name = entry.getKey();
                AddOns a = addOnRepo.findByName(name).get();
                Integer qty = entry.getValue();
                Double cost = a.getCost();
                addOnTotal +=  cost * qty;
                addOn.put(a, qty);
            }

            LocalDate checkIn = LocalDate.parse(bookingDto.getCheckIn());
            LocalDate checkOut = LocalDate.parse(bookingDto.getCheckOut());

            LocalTime checkInTime = LocalTime.parse(bookingDto.getCheckInTime());
            LocalTime checkOutTime = LocalTime.parse(bookingDto.getCheckOutTime());
            long durationDays = ChronoUnit.DAYS.between(checkIn, checkOut);

            Double costPerDay = room.getRoomType().getCostPerDay();
        

            addOnTotal = addOnTotal * durationDays;
            Double totalAmount = (durationDays * costPerDay) + addOnTotal;
            Booking booking = new Booking(room, cust, guestCnt, checkIn, checkOut, checkInTime, checkOutTime, totalAmount, 0d);

            booking.setAddOnMap(addOn);
            booking.setAddOnTotal(addOnTotal);

                statusService.updateStatus("reserved", room.getRoomNumber(), checkIn, checkOut, checkInTime, checkOutTime, cust, booking);
                

                bookingRepository.save(booking);
                logger.info("Booking updated: {}", booking.getBookingId());
                //Long id = ras.getId();
                

                return  booking;
            }

    @Transactional
    //add addOns to already existing booking
    public Long addAddOns(String addOnName, int qty, Long bookingId)
    {
        Booking booking = bookingRepository.findById(bookingId).get();
        Map<AddOns, Integer> map = new HashMap<>();
        AddOns addon = addOnRepo.findByName(addOnName).get();
        map.put(addon, qty);
        booking.setAddOnMap(map);
        bookingRepository.save(booking);
        return booking.getBookingId();
    }

    @Transactional
    //delete by bookng id
    public void deleteBooking(Long bookingId)
    {
        bookingRepository.deleteById(bookingId);
    }

    //get booking summary
    public BookingSummaryDto getBookingSummary(Long bookingId)
    {
        BookingSummaryDto response = new BookingSummaryDto();
        response.setBookingId(bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();
        LocalDate checkIn = booking.getCheckin();
        LocalDate checkOut = booking.getCheckout();
        Customer cust = booking.getCustomer();
        Double amount = booking.getTotalAmount();
        Double paid = booking.getPaidAmount();
        Double pending = booking.getPendingAmount();
        String custName = cust.getFirstName() + " "+ cust.getLastName();

        LocalTime checkInTime = booking.getCheckinTime();
        LocalTime checkOutTime = booking.getCheckoutTime();

        Map<AddOns, Integer> addOnMap = booking.getAddOnMap();
        Map<String, Integer> addOnSummary = new HashMap<>();
        for(Map.Entry<AddOns, Integer> entry: addOnMap.entrySet())
        {
            String name = entry.getKey().getName();
            Integer qty = entry.getValue();
            addOnSummary.put(name, qty);
        }

        Double cost = booking.getAddOnTotal();
        List<Guest> list = guestRepository.findByBooking(booking);
        List<String> guestList = new ArrayList<>();
        for(Guest guest: list)
        {
            String name = guest.getFirstName() + " "+ guest.getLastName();
            guestList.add(name);
        }
        Integer roomNumber = booking.getRoom().getRoomNumber();
        RoomConfig obj = booking.getRoom().getRoomType();
        String roomType = obj.getRoomType();
        String amenities = obj.getAmenities();

        response.setAmenities(amenities);
        response.setBookingId(bookingId);
        response.setCheckIn(checkIn);
        response.setCheckOut(checkOut);
        response.setCustomerName(custName);
        response.setGuestList(guestList);
        response.setPaidAmt(paid);
        response.setPendingAmt(pending);
        response.setTotal(amount);
        response.setRoomNumber(roomNumber);
        response.setRoomType(roomType);
        response.setAddOnMap(addOnSummary);
        response.setCheckInTime(checkInTime);
        response.setCheckOutTime(checkOutTime);
        response.setAddOnTotal(cost);

        return response;
    }

    @Transactional
    //add payment details
    public void addPayment(Long bookingId, String paymentMode,Double paidAmount)
    {
        Booking booking = bookingRepository.findById(bookingId).get();
        booking.setModeOfPayment(paymentMode);
        booking.setPaidAmount(paidAmount);
        bookingRepository.save(booking);
    }

    //find all bookngs
    public List<BookingSummaryDto> getAll()
    {
        List<Booking> bookingList = bookingRepository.findAll();
        
        return mapToSummaryList(bookingList);

        
    }
    //find by date
    public List<BookingDto> getByCheckInDate(String dateString)
    {
        LocalDate date = LocalDate.parse(dateString);
        List<Booking> bookingList = bookingRepository.findByCheckin(date);
        return mapToDtoList(bookingList);
        
    }
    //find by customer
    public List<BookingDto> getByCustomer(Long phone)
    {
        Optional<Customer> optional = customerRepository.findByPhoneNumber(phone);
        Customer cust = optional.get();
        List<Booking> list = bookingRepository.findByCustomer(cust);
        return mapToDtoList(list);
    }
    
    BookingDto mapToDto(Booking booking)
    {
        Integer roomNumber = booking.getRoom().getRoomNumber();
        Integer totalGuestNumber = booking.getTotalGuestCount();

        
        //Optional<Customer> optional = customerRepository.findByPhoneNumber(booking.getCustomer().getPhoneNumber());
        Optional<Customer> optional = customerRepository.findByCustomerId(booking.getCustomer().getCustomerId());
        if(optional.isPresent())
        {
            
        Customer cust = optional.get();
        LocalDate checkIn = booking.getCheckin();
        LocalDate checkOut = booking.getCheckout();

        LocalTime checkInTime = booking.getCheckinTime();
        LocalTime checkOutTime = booking.getCheckoutTime();

        Map<AddOns, Integer> map = booking.getAddOnMap();
        Map<String, Integer> addOnSummary = new HashMap<>();
        for(Map.Entry<AddOns, Integer> entry: map.entrySet())
        {
            String name = entry.getKey().getName();
            Integer qty = entry.getValue();
            addOnSummary.put(name, qty);
        }

        BookingDto dto = new BookingDto(roomNumber, cust.getCustomerId(), totalGuestNumber, checkIn.toString(), 
        checkOut.toString(), checkInTime.toString(), checkOutTime.toString(), addOnSummary);
        dto.setBookingId(booking.getBookingId());
        return dto;
    }
        return null;
    }

    List<BookingDto> mapToDtoList(List<Booking> bookingList)
    {
            return bookingList.stream()
                    .map(booking -> mapToDto(booking))
                    .collect(Collectors.toList());
    }
    
    Booking mapToObj(BookingDto bookDto)
    {
        Integer roomNumber = bookDto.getRoomNumber();
        Room room = roomRepo.findByRoomNumber(roomNumber).get();
        Long custId = bookDto.getCustId();
        Customer cust = customerRepository.findById(custId).get();
        //Customer cust = optional.get();
        Integer totalGuest = bookDto.getTotalGuestCount();
        
        LocalDate checkIn = LocalDate.parse(bookDto.getCheckIn());
        LocalDate checkOut = LocalDate.parse(bookDto.getCheckOut());
        long durationDays = ChronoUnit.DAYS.between(checkIn, checkOut);

        LocalTime checkInTime = LocalTime.parse(bookDto.getCheckInTime());
        LocalTime checkOutTime = LocalTime.parse(bookDto.getCheckOutTime());

        Map<String, Integer> map = bookDto.getAddOnMap();
        Map<AddOns, Integer> addOn = new HashMap<>();
        Double addOnTotal = 0d;
        for(Map.Entry<String, Integer> entry: map.entrySet())
        {
            String name = entry.getKey();
            AddOns a = addOnRepo.findByName(name).get();
            Integer qty = entry.getValue();
            Double cost = a.getCost();
            addOnTotal +=  cost * qty;
            addOn.put(a, qty);
        }


        Double costPerDay = room.getRoomType().getCostPerDay();
        Double totalAmount = (durationDays * costPerDay) + addOnTotal;

        Booking booking = new Booking(room, cust , totalGuest, checkIn, checkOut, checkInTime, checkOutTime, totalAmount, 0d);
        booking.setAddOnMap(addOn);
        booking.setAddOnTotal(addOnTotal);

        return booking;

    }

    

    BookingSummaryDto mapToSummary(Booking booking)
    {
        BookingSummaryDto response = new BookingSummaryDto();
        LocalDate checkIn = booking.getCheckin();
        LocalDate checkOut = booking.getCheckout();
        Customer cust = booking.getCustomer();
        Double amount = booking.getTotalAmount();
        Double paid = booking.getPaidAmount();
        Double pending = booking.getPendingAmount();
        String custName = cust.getFirstName() + " "+ cust.getLastName();

        LocalTime checkInTime = booking.getCheckinTime();
        LocalTime checkOutTime = booking.getCheckoutTime();

        Map<AddOns, Integer> addOnMap = booking.getAddOnMap();
        Map<String, Integer> addOnSummary = new HashMap<>();
        for(Map.Entry<AddOns, Integer> entry: addOnMap.entrySet())
        {
            String name = entry.getKey().getName();
            Integer qty = entry.getValue();
            addOnSummary.put(name, qty);
        }

        Double cost = booking.getAddOnTotal();
        List<Guest> list = guestRepository.findByBooking(booking);
        List<String> guestList = new ArrayList<>();
        for(Guest guest: list)
        {
            String name = guest.getFirstName() + " "+ guest.getLastName();
            guestList.add(name);
        }
        Integer roomNumber = booking.getRoom().getRoomNumber();
        RoomConfig obj = booking.getRoom().getRoomType();
        String roomType = obj.getRoomType();
        String amenities = obj.getAmenities();

        Long bookingId = booking.getBookingId();
        response.setAmenities(amenities);
        response.setBookingId(bookingId);
        response.setCheckIn(checkIn);
        response.setCheckOut(checkOut);
        response.setCustomerName(custName);
        response.setGuestList(guestList);
        response.setPaidAmt(paid);
        response.setPendingAmt(pending);
        response.setTotal(amount);
        response.setRoomNumber(roomNumber);
        response.setRoomType(roomType);
        response.setAddOnMap(addOnSummary);
        response.setCheckInTime(checkInTime);
        response.setCheckOutTime(checkOutTime);
        response.setAddOnTotal(cost);

        return response;
    }

    List<BookingSummaryDto> mapToSummaryList(List<Booking> list)
    {
        return list.stream()
            .map(this :: mapToSummary)
            .collect(Collectors.toList());
    }
    

    @Transactional
    public Long updateBooking(Long bookingId, BookingDto bookingDto)
    {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if(!bookingOpt.isPresent())
        {
            return null;
        }
        Booking booking = bookingOpt.get();
        Optional<Customer> opt = customerRepository.findById(bookingDto.getCustId());
        if(!opt.isPresent())
        {
            return null;
        }
        booking.setCustomer(opt.get());
        return bookingId;
    }

    //date in string format : 2024-01-23
    @Transactional
    public Long editBooking(Long bookingId, NestedResponse dto, String dateString) throws Exception
    {
        Booking booking = bookingRepository.findById(bookingId).get();
        Room room = booking.getRoom();
        //RoomAvailabilityStatusDto rasDto = statusService.getNextCheckInForRoom(room.getRoomNumber(), LocalDate.now());

        LocalDate checkOutNew = LocalDate.parse(dateString);
        boolean exist = ifAlreadyBooked(room.getRoomNumber(), booking.getCheckout(), checkOutNew);
        if(!exist)
        {

            booking.setCheckout(checkOutNew);
            bookingRepository.save(booking);
            return bookingId;

        }
        else
        {
            return null;
        }
    }
}



