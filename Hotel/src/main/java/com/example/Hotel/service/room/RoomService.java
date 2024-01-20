package com.example.Hotel.service.room;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.room.Room;
import com.example.Hotel.entity.room.RoomAvailabilityStatus;
import com.example.Hotel.entity.room.RoomAvailabilityStatusDto;
import com.example.Hotel.entity.room.RoomConfig;
import com.example.Hotel.entity.room.RoomDto;
import com.example.Hotel.entity.room.RoomStatusChangeEvent;
import com.example.Hotel.repository.reservation.BookingRepository;
import com.example.Hotel.repository.room.RoomAvailabilityStatusRepository;
import com.example.Hotel.repository.room.RoomConfigRepository;
import com.example.Hotel.repository.room.RoomRepository;
import com.example.Hotel.service.task.TaskService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoomService {

    @Autowired
    RoomStatusService roomStatusService;
    @Autowired
    RoomAvailabilityStatusRepository statusRepo;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomConfigRepository roomConfigRepository;

    @Autowired
    BookingRepository bookingRepo;

    @Autowired
    TaskService taskService;
    //initialise rooms

    @Transactional
    //add rooms
    public void addRoom(RoomDto roomDto)
    {
        Room room = mapToObj(roomDto);
        roomRepository.save(room);
    }
    //get all rooms
    public List<RoomDto> getAllRooms()
    {
        List<Room> room = roomRepository.findAll();
        return mapToDtoList(room);
    }
    //get all rooms by status : vacant/ reserved/ occcupied/ dirty/ outOforder
    public List<RoomDto> getByStatus(String statusString)
    {
        List<Room> room = roomRepository.findByRoomStatus(statusString);
        return mapToDtoList(room);
    }
    //getBy room number
    public RoomDto getByRoomNumber(Integer roomNumber)
    {
        Room room = roomRepository.findByRoomNumber(roomNumber).get();
        return mapToDto(room);
    }
    //get by view
    public List<RoomDto> getByView(String view)
    {
        List<Room> list = roomRepository.findByViewType(view);
        return mapToDtoList(list);
    }
    //get by bed
    public List<RoomDto> getByBed(String bed)
    {
        List<Room> list = roomRepository.findByBedType(bed);
        return mapToDtoList(list);
    }
    //get by floor
    public List<RoomDto> getByFloor(String floor)
    {
        List<Room> list = roomRepository.findByFloor(floor);
        return mapToDtoList(list);
    }

    //customer checked in
    @Transactional
    public void checkInStatus(Long bookingId) throws Exception
    {
        Optional<Booking> opt = bookingRepo.findByBookingId(bookingId);
        if(!opt.isPresent())
            throw new Exception("Booking id Not found!");
        Booking b = opt.get();
        Room room = b.getRoom();
        //RoomAvailabilityStatus ras = statusRepo.findByRoomAndCheckinDateAndCheckoutDate(room, b.getCheckin(), b.getCheckout());
        Optional<RoomAvailabilityStatus> rasOpt = statusRepo.findByBooking(b);
        if(rasOpt.isEmpty())
        {
            throw new Exception("Booking id Not found!");
        }
        RoomAvailabilityStatus ras = rasOpt.get();
        ras.setCheckinDate(b.getCheckin());
        ras.setCheckinTime(b.getCheckinTime());
        ras.setCheckoutDate(b.getCheckout());
        ras.setCheckoutTime(b.getCheckoutTime());
        ras.setStatus("occupied");
        room.setRoomStatus("occupied");
    }

    //customer checked out
    //when customer has checked out the room status will be dirty and new cleaing task will be created for
    //that room
    @Transactional
    public void checkOutStatus(Long bookingId) throws Exception
    {
        Optional<Booking> opt = bookingRepo.findByBookingId(bookingId);
        if(!opt.isPresent())
            throw new Exception("Booking id Not found!");
        
        Booking b = opt.get();
        Room room = b.getRoom();
        //RoomAvailabilityStatus ras = statusRepo.findByRoomAndCheckinDateAndCheckoutDate(room, b.getCheckin(), b.getCheckout());
        RoomAvailabilityStatus ras = statusRepo.findByBooking(b).get();
        
        ras.setCheckinDate(null);
        //ras.setCheckinTime(null);
        ras.setCheckoutDate(null);
        //ras.setCheckoutTime(null);

        ras.setStatus("dirty");
        room.setRoomStatus("dirty");


        RoomAvailabilityStatusDto dto = roomStatusService.getNextCheckInForRoom(room.getRoomNumber(), LocalDate.now());
        if(dto != null)
        {
            RoomAvailabilityStatus obj = roomStatusService.mapDtoToObj(dto);
            if(obj != null)
            {
            //RoomAvailabilityStatus obj = optional.get();
            LocalTime currentTime = LocalTime.now();
            LocalTime nextCheckIn = obj.getCheckinTime();
            Duration duration = Duration.between(currentTime, nextCheckIn);
            if(duration.toHours() > 3l)//if duration between next check in and current checkout time is more than 2 hours
            {
                taskService.createAutoTask("Medium", room.getRoomNumber(), LocalTime.MAX,"Cleaning");
            }
            }
            else
            {
                taskService.createAutoTask("High", room.getRoomNumber(), LocalTime.MAX,"Cleaning");
            }
            
        }
        else
        {
            taskService.createAutoTask("Low", room.getRoomNumber(), LocalTime.MAX,"Cleaning");
        }
        //r

    }

    

    @Transactional
    //delete room by room Number
    public void deleteByRoomNumber(Integer roomNumber) throws Exception
    {
        if(roomRepository.findByRoomNumber(roomNumber) == null)
            throw new Exception("No such room found!");
        roomRepository.deleteByRoomNumber(roomNumber);
    }

    
    @Transactional
    //update status
    public void updateStatus(String newStatus, Integer roomNumber,LocalDate checkIn, LocalDate checkOut, LocalTime checkInTime, LocalTime checkOutTime)
    {
        
        Room room = roomRepository.findByRoomNumber(roomNumber).get();
        room.setRoomStatus(newStatus);
        publishRoomStatusChangeEvent(room, newStatus,checkIn, checkOut, checkInTime, checkOutTime);
        roomRepository.save(room);
    }

    private void publishRoomStatusChangeEvent(Room room, String newStatus, LocalDate checkIn, LocalDate checkOut, LocalTime checkInTime, LocalTime checkOutTime) {
        // Create and publish the event
        
        RoomStatusChangeEvent event = new RoomStatusChangeEvent(room, newStatus, checkIn, checkOut, checkInTime, checkOutTime);
        applicationEventPublisher.publishEvent(event);
    }


    //map dto to obj
    public Room mapToObj(RoomDto roomDto) {
        Integer roomNumber = roomDto.getRoomNumber();
        String status = roomDto.getRoomStatus();
        Optional<RoomConfig> roomConfig = roomConfigRepository.findByRoomType(roomDto.getRoomType());
        String view = roomDto.getViewType();
        String bathroom = roomDto.getBathroom();
        String floor = roomDto.getFloor();
        String bed = roomDto.getBedType();
        if (roomConfig.isPresent()) {
            Room room = new Room(roomNumber, roomConfig.get(), status, bed, view, bathroom, floor);
            return room;
        }

        return null;
    }
    //map obj to dto
    public RoomDto mapToDto(Room room) {
        String status = room.getRoomStatus();
        String type = room.getRoomType().getRoomType();
        Integer roomNumber = room.getRoomNumber();

        String view = room.getViewType();
        String bathroom = room.getBathroomType();
        String floor = room.getFloor();
        String bed = room.getBedType();

        RoomDto roomDto = new RoomDto(roomNumber, type, status, bed, view, bathroom, floor);
        return roomDto;
    }

    //map dto list to obj list
    // Map object list to DTO list
    public List<RoomDto> mapToDtoList(List<Room> roomList) {
        return roomList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}

/*//check if available for check in - check out
    public boolean isRoomAvailable(Integer roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = roomRepository.findByRoomNumber(roomNumber);
        Optional<RoomAvailabilityStatus> optional = statusRepo.findByRoom(room);

        RoomAvailabilityStatus existingStatus = optional.get();

        
            LocalDate existingCheckIn = existingStatus.getCheckinDate();

            LocalDate existingCheckOut = existingStatus.getCheckoutDate();

            if ((checkInDate.isAfter(existingCheckIn) && checkInDate.isBefore(existingCheckOut)) ||
                (checkOutDate.isAfter(existingCheckIn) && checkOutDate.isBefore(existingCheckOut)) ||
                (checkInDate.isBefore(existingCheckIn) && checkOutDate.isAfter(existingCheckOut)) ||
                (checkInDate.isEqual(existingCheckIn) || checkOutDate.isEqual(existingCheckOut))) {
                // Overlapping booking found
                return false;
            }
        

        // No overlapping bookings found
        return true;
    }
*/

  /*  //map obj list to dto list
    // Map DTO list to object list
    public List<Room> mapToObjList(List<RoomDto> dtoList) {
        return dtoList.stream()
                .map(this::mapToObj)
                .collect(Collectors.toList());
    }
    */
