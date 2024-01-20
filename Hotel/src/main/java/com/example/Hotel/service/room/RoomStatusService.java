package com.example.Hotel.service.room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.guest.Customer;
import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.room.Room;
import com.example.Hotel.entity.room.RoomAvailabilityStatus;
import com.example.Hotel.entity.room.RoomAvailabilityStatusDto;
import com.example.Hotel.entity.room.RoomConfig;
import com.example.Hotel.repository.reservation.BookingRepository;
import com.example.Hotel.repository.room.RoomAvailabilityStatusRepository;
import com.example.Hotel.repository.room.RoomRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoomStatusService {
    
    @Autowired
    RoomService roomService;

    @Autowired
    private RoomAvailabilityStatusRepository roomAvailabilityStatusRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    BookingRepository bookingRepo;


    

    /*getAllRooms
    public List<RoomAvailabilityStatusDto> getAllRooms()
    {
        List<Booking> bookingList = bookingRepo.findAll();
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findAll();
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findAll();


        
        List<RoomAvailabilityStatusDto> dtoList = list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return dtoList;
    }*/

    //get All rooms with customer name
    public List<RoomAvailabilityStatusDto> getAllRoomsWithCustomer()
    {
        //List<RoomAvailabilityStatusDto> list = getCurrentStatusForAllRooms(LocalDate.now());

        Set<RoomAvailabilityStatus> list = new HashSet<>(); //= roomAvailabilityStatusRepository.findByRoom_RoomStatus("vacant");
        list.addAll(roomAvailabilityStatusRepository.findByRoom_RoomStatus("vacant"));
        list.addAll(roomAvailabilityStatusRepository.findByCheckinDateBeforeAndCheckoutDateAfter(LocalDate.now(), LocalDate.now()));
        list.addAll(roomAvailabilityStatusRepository.findByStatusOrCheckoutDate("dirty", LocalDate.now()));
        //list.addAll(roomAvailabilityStatusRepository.findByStatusAndCheckinDateAfter("vacant", LocalDate.now()));
        //list.addAll(roomAvailabilityStatusRepository.findByCheckinDateBetween(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
        //list.addAll(roomAvailabilityStatusRepository.findByStatusOrCheckoutDate("dirty", LocalDate.now()));
        //list.addAll(roomAvailabilityStatusRepository.findByStatusOrCheckoutDate("outoforder", LocalDate.now()));
        //list.addAll(roomAvailabilityStatusRepository.findByCheckinDateBeforeAndCheckoutDateAfter(LocalDate.now(), LocalDate.now()));//occupied
//        list.addAll(roomAvailabilityStatusRepository.findByCheckinDateIsNullAndCheckoutDateIsNull());

        list.addAll(initialiseRoomStatus());
        List<RoomAvailabilityStatus> ras = new ArrayList<>();
        ras.addAll(list);
        ras = sort(ras);
        List<RoomAvailabilityStatusDto> dtoL = ras.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());

        return dtoL;
    }

    private List<RoomAvailabilityStatus> initialiseRoomStatus()
    {
        List<Room> roomsList = roomRepository.findByRoomStatus("vacant");
        List<RoomAvailabilityStatus> list = new ArrayList<>();;
        for(Room r : roomsList)
        {
            RoomAvailabilityStatus roomAvailabilityStatus = new RoomAvailabilityStatus();
            roomAvailabilityStatus.setRoom(r);
            roomAvailabilityStatus.setStatus("vacant");
            roomAvailabilityStatus.setCheckinDate(null);
            roomAvailabilityStatus.setCheckoutDate(null);
            roomAvailabilityStatus.setCheckinTime(null);
            roomAvailabilityStatus.setCheckoutTime(null);
            list.add(roomAvailabilityStatus);
        }
        return list;
     }

        

    

    RoomAvailabilityStatusDto mapToDto(RoomAvailabilityStatus r)
    {
        RoomAvailabilityStatusDto obj = new RoomAvailabilityStatusDto();
            obj.setCheckinDate(r.getCheckinDate());
            obj.setCheckinTime(r.getCheckinTime());
            obj.setCheckoutDate(r.getCheckoutDate());
            obj.setCheckoutTime(r.getCheckoutTime());
            obj.setRoomNumber(r.getRoom().getRoomNumber());
            obj.setStatus(r.getStatus());
            //obj.setCustomerName();
            Customer c = r.getCustomer();
            String cname = c.getFirstName()+ " "+ c.getLastName();
            obj.setCustomerName(cname);
            //Optional<Booking> b = bookingRepository.findByRoom(room);
            //Customer c = b.map(Booking::getCustomer).orElse(null);

            RoomConfig rConfig = r.getRoom().getRoomType();
            obj.setRoomType(rConfig.getRoomType());
            obj.setAmenities(rConfig.getAmenities());
            obj.setCostPerDay(rConfig.getCostPerDay());

            obj.setBathroomType(r.getRoom().getBathroomType());
            obj.setBedType(r.getRoom().getBedType());
            obj.setViewType(r.getRoom().getViewType());
            obj.setFloor(r.getRoom().getFloor());

            return obj;

    }

/*     public RoomAvailabilityStatusResponse mapToStatusDto(RoomAvailabilityStatus r) {
        RoomAvailabilityStatusDto obj = new RoomAvailabilityStatusDto();
        obj.setCheckinDate(r.getCheckinDate());
        obj.setCheckinTime(r.getCheckinTime());
        obj.setCheckoutDate(r.getCheckoutDate());
        obj.setCheckoutTime(r.getCheckoutTime());
    
        Room room = r.getRoom();
        obj.setRoomNumber(room.getRoomNumber());
    
        RoomConfig rConfig = room.getRoomType();
        obj.setRoomType(rConfig.getRoomType());
        obj.setAmenities(rConfig.getAmenities());
        obj.setCostPerDay(rConfig.getCostPerDay());
    
        obj.setBathroomType(room.getBathroomType());
        obj.setBedType(room.getBedType());
        obj.setViewType(room.getViewType());
        obj.setFloor(room.getFloor());
        obj.setStatus(r.getStatus());
    
        Customer c = null;
        if (r.getBooking() != null) {
            Optional<Booking> b = bookingRepository.findByBookingId(r.getBooking().getBookingId());
            c = b.map(Booking::getCustomer).orElse(null);
        }
    
        return Pair.of(obj, c); // Consider using Optional for Customer if it can be null
    }

    Pair<RoomAvailabilityStatusDto, Customer> mapToStatusDto(RoomAvailabilityStatus r)
    {
            RoomAvailabilityStatusDto obj = new RoomAvailabilityStatusDto();
            obj.setCheckinDate(r.getCheckinDate());
            obj.setCheckinTime(r.getCheckinTime());
            obj.setCheckoutDate(r.getCheckoutDate());
            obj.setCheckoutTime(r.getCheckoutTime());
            Room room = r.getRoom();
            Optional<Booking> b = bookingRepository.findByBookingId(r.getBooking());
            Customer c = b.map(Booking::getCustomer).orElse(null);

            RoomConfig rConfig = room.getRoomType();
            obj.setRoomType(rConfig.getRoomType());
            obj.setAmenities(rConfig.getAmenities());
            obj.setCostPerDay(rConfig.getCostPerDay());

            obj.setBathroomType(room.getBathroomType());
            obj.setBedType(room.getBedType());
            obj.setViewType(room.getViewType());
            obj.setFloor(room.getFloor());

            if(c == null)
                c = new Customer();
            obj.setStatus(r.getStatus());
            obj.setRoomNumber(room.getRoomNumber());
            return Pair.of(obj, c);
    }
*/
    List<RoomAvailabilityStatusDto> maptoDtoList(List<RoomAvailabilityStatus> list) {
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //check In done
    public void checkInStatus(Long bookingId) throws Exception
    {
        roomService.checkInStatus(bookingId);
    }

    //check out done
    public void checkOutStatus(Long bookingId) throws Exception
    {
        roomService.checkOutStatus(bookingId);
    }

     @Transactional
    public void updateStatus(String status, Integer roomNumber, LocalDate checkIn, LocalDate checkOut, LocalTime checkInTime, LocalTime checkOutTime, Customer cust, Booking booking)
    {
        
            Optional<Room> roomOpt = roomRepository.findByRoomNumber(roomNumber);
            if(roomOpt.isPresent())
            {
                Room room = roomOpt.get();
                if(!room.getRoomStatus().equalsIgnoreCase("OutOfOrder"))
                {
                        RoomAvailabilityStatus r = new RoomAvailabilityStatus();
                            r.setRoom(room);
                            if(checkIn.equals(LocalDate.now()))
                                room.setRoomStatus("reserved");
                            else
                                room.setRoomStatus("vacant");
                            r.setCheckinDate(checkIn);
                            r.setCheckinTime(checkInTime);
                            r.setCheckoutTime(checkOutTime);
                            r.setCheckoutDate(checkOut);
                            r.setStatus(status);
                            r.setCustomer(cust);
                            r.setBooking(booking);
                            roomRepository.save(room);
                            roomAvailabilityStatusRepository.save(r);
                    }
                }
                
    }


    public List<RoomAvailabilityStatusDto> getVacantRoomsForDateTimeRange(LocalDate checkInDate, LocalTime checkInTime, LocalDate checkOutDate, LocalTime checkOutTime) {
        List<RoomAvailabilityStatus> occupiedOrReservedRooms = roomAvailabilityStatusRepository.findOccupiedOrReservedRoomsForDateTimeRange(checkInDate, checkInTime, checkOutDate, checkOutTime);
        occupiedOrReservedRooms.addAll(roomAvailabilityStatusRepository.findByCheckoutDateAndCheckoutTimeBetween(checkOutDate, checkInTime, checkOutTime));
        occupiedOrReservedRooms.addAll(roomAvailabilityStatusRepository.findByCheckinDateBetween(checkInDate, checkOutDate));
        //List<RoomAvailabilityStatus> occupiedOrReservedRooms = roomAvailabilityStatusRepository.findByCheckinDateAndCheckoutDateNotBetween(checkInDate, checkOutDate);
        //occupiedOrReservedRooms.addAll(roomAvailabilityStatusRepository.find)
        Set<Integer> occupiedOrReservedRoomNumbers = occupiedOrReservedRooms.stream()
            .map(ras -> ras.getRoom().getRoomNumber())
            .collect(Collectors.toSet());
    
        List<Room> allRooms = roomRepository.findAll();
        return allRooms.stream()
            .filter(room -> !occupiedOrReservedRoomNumbers.contains(room.getRoomNumber()))
            .map(this::mapRoomToDefaultStatusDto)
            .collect(Collectors.toList());
    }
    
    private RoomAvailabilityStatusDto mapRoomToDefaultStatusDto(Room room) {
        RoomAvailabilityStatusDto dto = new RoomAvailabilityStatusDto();
        dto.setRoomNumber(room.getRoomNumber());
        dto.setStatus("vacant");
        dto.setAmenities(room.getRoomType().getAmenities());
        dto.setBathroomType(room.getBathroomType());
        dto.setCostPerDay(room.getRoomType().getCostPerDay());
        dto.setRoomType(room.getRoomType().getRoomType());
        dto.setBedType(room.getBedType());
        dto.setFloor(room.getFloor());
        dto.setViewType(room.getViewType());
        dto.setCustomerName(null);
        dto.setBookingId(null);
        dto.setCheckinDate(null);
        dto.setCheckinTime(null);
        dto.setCheckoutDate(null);
        dto.setCheckoutTime(null);
        // Set other fields with default or null values
        return dto;
    }
    

    

    public List<RoomAvailabilityStatusDto> getAllReservedRooms() {
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatus("reserved");
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByCheckinDateAfter(LocalDate.now());
        
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RoomAvailabilityStatusDto> getReservedRoomsForDateRange(LocalDate startDate, LocalDate endDate) {
        //List<RoomAvailabilityStatus> list =roomAvailabilityStatusRepository.findByStatusAndCheckinDateBetween("reserved", startDate, endDate);
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusAndCheckinDateBetween("reserved", startDate, endDate);// findByCheckinDateBetween(startDate.minusDays(1), endDate.plusDays(1));
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RoomAvailabilityStatusDto> getUpcomingCheckIns(LocalDate date) {
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findAllUpcomingCheckIns(date);
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByCheckinDateAfterOrderByCheckinDateAsc(date);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RoomAvailabilityStatusDto> getRoomsDueOutNextHours(LocalDate date, LocalTime time, int hours) {
        LocalTime endTime = LocalTime.now().plusHours(hours);
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusAndCheckoutDateLessThanEqualAndCheckoutTimeLessThanEqual("occupied", date, time.plusHours(hours));
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByCheckoutDateAndCheckoutTimeBetween(date, time, endTime);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    // public List<RoomAvailabilityStatusDto> getCurrentlyVacantRooms() {
    //     Set<RoomAvailabilityStatus> list = new HashSet<>();//= roomAvailabilityStatusRepository.findByStatusAndCheckinDateIsNullAndCheckoutDateIsNull("vacant");
    //     list.addAll(roomAvailabilityStatusRepository.findByRoom_RoomStatus("vacant"));
    //     list.addAll(roomAvailabilityStatusRepository.findByCheckinDateAfterOrderByCheckinDateAsc(LocalDate.now()));
    //     //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusAndCheckinDateAndCheckoutDate("vacant", LocalDate.now(), LocalDate.now());
    //     return list.stream().map(this::mapToDto).collect(Collectors.toList());
    // }

    public List<RoomAvailabilityStatusDto> getDirtyRooms(LocalDate currentDate) {
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusAndCheckinDate("dirty", currentDate);
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusOrCheckoutDate("dirty", currentDate);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RoomAvailabilityStatus> getOccupiedRooms() {
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatus("occupied");
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusOrCheckinDateBeforeAndCheckoutDateAfter("occupied", LocalDate.now(), LocalDate.now());
        return list;
        //return list.stream().map(this::mapToDto).collect(Collectors.toList());

    }

    public List<RoomAvailabilityStatusDto> getOutOfOrderRooms() {
        //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatus("outoforder");
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByRoom_RoomStatus("outoforder");
        return list.stream().map(this::mapToDto).collect(Collectors.toList());

    }

    public List<RoomAvailabilityStatusDto> getUpcomingCheckout(LocalDate currentDate) {
        //List<RoomAvailabilityStatus> list =  roomAvailabilityStatusRepository.findByCheckoutDate(currentDate);
        List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByCheckinDateBeforeAndCheckoutDateAfter(currentDate, currentDate);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public RoomAvailabilityStatusDto getNextCheckInForRoom(Integer roomNumber, LocalDate currentDate) {
        Room room = roomRepository.findByRoomNumber(roomNumber).get();
            List<RoomAvailabilityStatus> upcomingCheckIns = roomAvailabilityStatusRepository.findAllUpcomingCheckInsForRoom(room, currentDate);

    
            //List<RoomAvailabilityStatus> upcomingCheckIns = roomAvailabilityStatusRepository.findByRoomAndCheckinDateAfter(room, currentDate);
            // Check if there are any upcoming check-ins and return the first one if present
            if (!upcomingCheckIns.isEmpty()) {
                return mapToDto(upcomingCheckIns.get(0));
                //return ; // The first element is the next check-in
            }
            return null; // Return null if there are no upcoming check-ins
        }
    
    // public List<RoomAvailabilityStatusDto> getVacantRoomsForDateTimeRange(LocalDate checkInDate, LocalTime checkInTime, LocalDate checkOutDate, LocalTime checkOutTime) {
    //     //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findVacantRoomStatusesForDateTimeRange(checkInDate, checkInTime, checkOutDate, checkOutTime);
    //     List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository.findByStatusAndCheckinDateBetween("reserved", checkInDate, checkOutDate);
    //     Set<Room> roomList = list.stream().map(room -> getRoom().getRoomNumber())
    //     .collect(Collectors.toList());

    //     //List bookedRooms = roomAvailabilityStatusRepository.findVacantRoomStatusesForDateTimeRange(checkInDate, checkInTime, checkOutDate, checkOutTime);
    //     //list = 
    //     //List<RoomAvailabilityStatus> list = roomAvailabilityStatusRepository
    //     return list.stream().map(this::mapToDto).collect(Collectors.toList());
    // }

    public List<RoomAvailabilityStatusDto> getCurrentStatusForAllRooms(LocalDate date) {
        List<RoomAvailabilityStatus> roomStatusSet = new ArrayList<>();
    
        // Fetch various statuses
        roomStatusSet.addAll(roomAvailabilityStatusRepository.findByCheckinDateBeforeAndCheckoutDateAfter(date, date));
        roomStatusSet.addAll(roomAvailabilityStatusRepository.findByCheckinDateIsNullAndCheckoutDateIsNull());
        roomStatusSet.addAll(roomAvailabilityStatusRepository.findByStatus("dirty"));
        roomStatusSet.addAll(roomAvailabilityStatusRepository.findByStatus("occupied"));
        roomStatusSet.addAll(roomAvailabilityStatusRepository.findByStatus("vacant"));
        //roomStatusSet.addAll(roomAvailabilityStatusRepository.findByStatus("reserved"));

        Map<Integer, RoomAvailabilityStatus> latestStatusMap = new HashMap<>();
        for (RoomAvailabilityStatus ras : roomStatusSet) {
            Integer roomNumber = ras.getRoom().getRoomNumber();
            LocalDate checkoutDate = ras.getCheckoutDate();
            LocalTime checkoutTime = ras.getCheckoutTime();
            
            boolean isMoreRecent = checkoutDate != null && (checkoutDate.isAfter(date) || 
                (checkoutDate.isEqual(date) && checkoutTime != null && checkoutTime.isAfter(LocalTime.now())));
            
            RoomAvailabilityStatus currentStatus = latestStatusMap.get(roomNumber);
            if (currentStatus == null || isMoreRecent) {
                latestStatusMap.put(roomNumber, ras);
            }
        }
    
        List<Room> allRooms = roomRepository.findByRoomStatus("vacant");
        allRooms.addAll(roomRepository.findByRoomStatus("reserved"));
        allRooms.addAll(roomRepository.findByRoomStatus("dirty"));
        allRooms.addAll(roomRepository.findByRoomStatus("occupied"));

    
            List<RoomAvailabilityStatusDto> allRoomStatuses = allRooms.stream()
            .map(room -> {
                RoomAvailabilityStatus latestStatus = latestStatusMap.get(room.getRoomNumber());
                if (latestStatus != null) {
                    return mapToDto1(latestStatus);
                } else {
                    // If no status is found, assume the room is vacant
                    return mapRoomToDefaultStatusDto(room);
                }
            })
            .collect(Collectors.toList());
    
        // Sort the DTOs by room number
        //allRoomStatuses.sort(Comparator.comparingInt(RoomAvailabilityStatusDto::getRoomNumber));
        return allRoomStatuses;
    }

    // //working without name
    // public List<RoomAvailabilityStatusDto> getCurrentStatusForAllRooms(LocalDate date) {
    //     List<RoomAvailabilityStatus> roomStatusList = new ArrayList<>(); //= roomAvailabilityStatusRepository.findAll();
    //     roomStatusList.addAll(roomAvailabilityStatusRepository.findByCheckinDateBeforeAndCheckoutDateAfter(date, date));
    //     roomStatusList.addAll(roomAvailabilityStatusRepository.findByCheckinDateIsNullAndCheckoutDateIsNull());
    //     roomStatusList.addAll(roomAvailabilityStatusRepository.findByStatus("dirty"));
    //     roomStatusList.addAll(roomAvailabilityStatusRepository.findByStatus("outoforder"));
    //     roomStatusList.addAll(roomAvailabilityStatusRepository.findByStatus("occupied"));
    //     roomStatusList.addAll(roomAvailabilityStatusRepository.findByStatus("vacant"));
    
    //     Map<Integer, RoomAvailabilityStatus> latestStatusMap = new HashMap<>();
    //     for (RoomAvailabilityStatus ras : roomStatusList) {
    //         Integer roomNumber = ras.getRoom().getRoomNumber();
    //         LocalDate checkoutDate = ras.getCheckoutDate();
    //         LocalTime checkoutTime = ras.getCheckoutTime();
    
    //         boolean isMoreRecent = checkoutDate != null && (checkoutDate.isAfter(date) || 
    //             (checkoutDate.isEqual(date) && checkoutTime != null && checkoutTime.isAfter(LocalTime.now())));
            
    //         if (!latestStatusMap.containsKey(roomNumber) || isMoreRecent) {
    //             latestStatusMap.put(roomNumber, ras);
    //         }
    //     }
    
    //     List<Room> allRooms = roomRepository.findAll();
    //     Set<Integer> roomNumbersInStatusSet = latestStatusMap.keySet();
    
    //     List<RoomAvailabilityStatusDto> allRoomStatuses = allRooms.stream()
    //         .map(room -> {
    //             Integer roomNumber = room.getRoomNumber();
    //             if (!roomNumbersInStatusSet.contains(roomNumber)) {
    //                 return mapRoomToDefaultStatusDto(room);
    //             } else {
    //                 RoomAvailabilityStatus status = latestStatusMap.get(roomNumber);
    //                 return mapToDto(status);
    //             }
    //         })
    //         .collect(Collectors.toList());
    
    //     allRoomStatuses.sort(Comparator.comparingInt(RoomAvailabilityStatusDto::getRoomNumber));
    //     return allRoomStatuses;
    // }
    
    
    
    private RoomAvailabilityStatusDto mapToDto1(RoomAvailabilityStatus ras) {
        RoomAvailabilityStatusDto dto = new RoomAvailabilityStatusDto();
        if (ras != null) {
            dto.setRoomNumber(ras.getRoom().getRoomNumber());
            dto.setStatus(ras.getStatus());
            Room room = ras.getRoom();
    
            // Set common fields
            RoomConfig rConfig = room.getRoomType();
            dto.setRoomType(rConfig.getRoomType());
            dto.setAmenities(rConfig.getAmenities());
            dto.setCostPerDay(rConfig.getCostPerDay());
            dto.setBathroomType(room.getBathroomType());
            dto.setBedType(room.getBedType());
            dto.setViewType(room.getViewType());
            dto.setFloor(room.getFloor());
    
            // Set booking and customer details
            if (ras.getBooking() != null) {
                dto.setBookingId(ras.getBooking().getBookingId());
                dto.setCheckinDate(ras.getCheckinDate());
                dto.setCheckinTime(ras.getCheckinTime());
                dto.setCheckoutDate(ras.getCheckoutDate());
                dto.setCheckoutTime(ras.getCheckoutTime());
                if (ras.getCustomer() != null) {
                    dto.setCustomerName(ras.getCustomer().getFirstName() + " " + ras.getCustomer().getLastName());
                } else {
                    dto.setCustomerName(null);
                }
            } else {
                dto.setBookingId(null);
                dto.setCustomerName(null);
                dto.setCheckinDate(null);
                dto.setCheckinTime(null);
                dto.setCheckoutDate(null);
                dto.setCheckoutTime(null);
            }
        }
        return dto;
    }
    
    
    
    
    List<RoomAvailabilityStatus> sort(List<RoomAvailabilityStatus> list)
    {
        return list.stream()
        .sorted(Comparator.comparingInt(status -> status.getRoom().getRoomNumber()))
        .collect(Collectors.toList());

    }
        
    // Scheduled method to update room statuses
    @Transactional
    @Scheduled(fixedRate = 3600000) //3600000) // Runs every hour (adjust the rate as needed)
    public void updateRoomStatus()
    {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentHour = LocalTime.now().truncatedTo(ChronoUnit.HOURS);
        //LocalTime nextHour = currentHour.plusHours(1);

        // Get rooms that are due out for the next hour
        List<RoomAvailabilityStatusDto> roomsDueOutNextHourDto = getRoomsDueOutNextHours(currentDate, currentHour, 12);

        List<RoomAvailabilityStatus> roomsDueOutNextHour = roomsDueOutNextHourDto.stream()
                .map(this::mapDtoToObj).collect(Collectors.toList());

        // Update room status to "due out" for rooms due out in the next hour
        roomsDueOutNextHour.forEach(room -> {
            room.setStatus("dueout");
            roomAvailabilityStatusRepository.save(room);
        });

        // Get rooms that are dirty (checked out within the last hour)
        //List<RoomAvailabilityStatus> roomsDirty = getRoomsDirty(currentDate, currentHour);

        // Update room status to "vacant" for rooms that were dirty but now vacant
        /*roomsDirty.forEach(room -> {
            room.setStatus("VACANT");
            roomAvailabilityStatusRepository.save(room);
        });*/
    }

    @Transactional
    @Scheduled(fixedRate = 3600000)//cron = "0 0 * * * *") // Runs every hour, adjust the cron expression as needed
    public void updateReservedRoomsStatusToOccupied() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Fetch rooms that are reserved and have the same check-in date
        List<RoomAvailabilityStatus> reservedRooms = roomAvailabilityStatusRepository.findByStatusAndCheckinDate("reserved", currentDate);

        // Update status to "occupied" for reserved rooms if the check-in date has arrived
        reservedRooms.stream()
                .filter(room -> room.getCheckinTime().isBefore(currentTime))
                .forEach(room -> {
                    room.setStatus("occupied");
                    roomAvailabilityStatusRepository.save(room);
                });
    
        // 1. Update rooms due out in the next 2 hours
        //List<RoomAvailabilityStatus> roomsDueOut = getRoomsDueOutNextHours(LocalDate.now(),LocalTime.now(),2);
        //roomsDueOut.forEach(room -> room.setStatus("due_out"));

        // 2. Update rooms with upcoming checkouts for today
        List<RoomAvailabilityStatusDto> upcomingCheckoutsdto = getUpcomingCheckout(LocalDate.now());
        List<RoomAvailabilityStatus> upcomingCheckouts = upcomingCheckoutsdto.stream()
                .map(this::mapDtoToObj).collect(Collectors.toList());
        upcomingCheckouts.forEach(room -> room.setStatus("dueout"));

        // 3. Update other status transitions as needed (add more conditions)
        //List<RoomAvailabilityStatusDto> dirtyRoomsdto = getDirtyRooms(LocalDate.now());
        //List<RoomAvailabilityStatus> dirtyRooms = dirtyRoomsdto.stream()
          //      .map(this::mapDtoToObj).collect(Collectors.toList());

        //Task task = taskService.getByRoom()
        //dirtyRooms.forEach(room -> {
            // Update status based on specific conditions, e.g., clean after certain time
          //  if (taskService.getByRoom(room.getRoom().getR.equalsIgnoreCase("completed")) {

                
  //          }
//        });

        // Save the changes to the database
        //roomAvailabilityStatusRepository.saveAll(roomsDueOut);
        roomAvailabilityStatusRepository.saveAll(upcomingCheckouts);
       // roomAvailabilityStatusRepository.saveAll(dirtyRooms);
    }


    public RoomAvailabilityStatus mapDtoToObj(RoomAvailabilityStatusDto dto)
    {
        RoomAvailabilityStatus ras = new RoomAvailabilityStatus();
        ras.setCheckinDate(dto.getCheckinDate());
        ras.setCheckinTime(dto.getCheckinTime());
        ras.setRoom(roomRepository.findByRoomNumber(dto.getRoomNumber()).get());
        ras.setStatus(dto.getStatus());
        return ras;
    }

    List<RoomAvailabilityStatus> mapToObjList(List<RoomAvailabilityStatusDto> dtoList)
    {
        return dtoList.stream()
        .map(this::mapDtoToObj)
        .collect(Collectors.toList());
    }


    //get all rooms with current status
   /*  List<RoomAvailabilityStatus> getAllCurrent()
    {
        List<RoomAvailabilityStatusDto> dtoList = getCurrentStatusForAllRooms(LocalDate.now());
        for(RoomAvailabilityStatusDto rasDto : dtoList)
        {
            RoomAvailabilityStatus ras = mapDtoToObj(rasDto);
            if(ras.getStatus().equalsIgnoreCase("vacant"))
            {
                Customer c= null;
            }
            else if(ras.getStatus().equalsIgnoreCase("reserved"))
            {
                Room room = ras.getRoom();
                bookingRepository.findB room.getRoomId();
            }
            else if(ras.getStatus().equalsIgnoreCase("occupied"))
            {

            }
        }
    }*/
}



    // // Get rooms that are due out for the next hour
    // public List<RoomAvailabilityStatus> getRoomsDueOutNextHour(LocalDate currentDate, LocalTime now) {
    //     return roomAvailabilityStatusRepository.findByStatusAndCheckoutDateAndCheckoutTimeBetween(
    //             "Occupied",
    //             currentDate,
    //             now,
    //             now.plusHours(1)
    //     );
    // }

    

//     // Get out of order rooms for a specific date and time range
//     public List<RoomAvailabilityStatus> getOutOfOrderRooms() {
//         return roomAvailabilityStatusRepository.findByStatusAndCheckinDateIsNullAndCheckoutDateIsNull("outoforder");
//     }

//     //get rooms for a specific checkIn date
//     public Optional<RoomAvailabilityStatus> getTodaysCheckInForRoom(LocalDate date, Integer roomNumber)
//     {
//         Room room = roomRepository.findByRoomNumber(roomNumber).get();
//         return roomAvailabilityStatusRepository.findByStatusAndCheckinDateAndRoom("dirty", date, room);
//     }*/
//     //get rooms with given checkout date

// /*

//     List<RoomAvailabilityStatusDto> mapTostatusDto(List<RoomAvailabilityStatus> list)
//     {
//         List<RoomAvailabilityStatusDto> dtoList = new ArrayList<>();
//         for(RoomAvailabilityStatus r: list)
//         {
//             RoomAvailabilityStatusDto obj = new RoomAvailabilityStatusDto();
//             obj.setRoomNumber(r.getRoom().getRoomNumber());
//             dtoList.add(obj);
//         }
//         return dtoList;
//     }
// */
    


