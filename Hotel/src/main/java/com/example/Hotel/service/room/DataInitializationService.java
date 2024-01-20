package com.example.Hotel.service.room;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.room.AddOns;
import com.example.Hotel.entity.room.Room;
import com.example.Hotel.entity.room.RoomConfig;
import com.example.Hotel.repository.room.AddOnRepository;
import com.example.Hotel.repository.room.RoomConfigRepository;
import com.example.Hotel.repository.room.RoomRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class DataInitializationService {


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    RoomConfigRepository roomTypeRepository;

    @Autowired
    private AddOnRepository addOnRepository;

    @PostConstruct
    @Transactional
    public void initializeDataIfNeeded() {
        if (!dataAlreadyInitialized()) {
            initializeData();
        }
       /*  if(!statusInitialised())
        {
            if(roomRepository.findByRoomStatus("vacant").size() > 0)
                initialiseRoomStatus();
        }*/
        if(!addOnInitialised())
        {
            initialiseAddOn();

        }
    }

    private boolean addOnInitialised()
    {
        if(addOnRepository.findAll().size() > 0)
            return true;
        else
            return false;
    }

    // private boolean statusInitialised()
    // {
    //     int size = statusRepository.findAll().size();
    //     if(size > 0)
    //         return true;
    //     else
    //         return false;
    // }

    private boolean dataAlreadyInitialized() {
        // Check if a sample room exists
        int size =  roomRepository.findAll().size();
        if(size > 0)
            return true;
        else
            return false;
    }

    private void initializeData() {
        // Create room types
        RoomConfig ACType = new RoomConfig();
        ACType.setRoomType("AC");
        ACType.setAmenities("AC TV Geyser Parking");
        ACType.setCostPerDay(3000d);
        roomTypeRepository.save(ACType);

        RoomConfig NonAcType = new RoomConfig();
        NonAcType.setRoomType("Non AC");
        NonAcType.setAmenities("TV Geyser Parking Breakfast");
        NonAcType.setCostPerDay(2100d);
        roomTypeRepository.save(NonAcType);

        RoomConfig deluxeType = new RoomConfig();
        deluxeType.setRoomType("Deluxe");
        deluxeType.setAmenities("AC TV Geyser Parking Breakfast");
        deluxeType.setCostPerDay(3000d);
        roomTypeRepository.save(deluxeType);


        int roomNo = 101;
        for(int i = 1; i <= 10; i++)
        {
            Room room = new Room();
            room.setRoomNumber(roomNo++);
            room.setRoomType(ACType);
            if(i % 5 == 0)
            {
                room.setRoomStatus("outoforder");
            }
            else
            {
                room.setRoomStatus("vacant");
                if(i % 4 == 0)
                {
                    room.setBedType("twin");
                    room.setViewType("parking");
                    room.setBathroomType("indian");
                }
                else if(i % 2 == 0)
                {
                    room.setBedType("queen");
                    room.setViewType("city");
                    room.setBathroomType("western");
                }
                else if(i % 3 == 0)
                {
                    room.setBedType("twin");
                    room.setViewType("parking");
                    room.setBathroomType("western");
                }
                else
                {
                    room.setBedType("queen");
                    room.setViewType("city");
                    room.setBathroomType("indian");
                }
            }
            
            room.setFloor("1");
            roomRepository.save(room);
        }

        roomNo = 201;
        for(int i = 1; i <= 10; i++)
        {
            Room room = new Room();
            room.setRoomNumber(roomNo ++);
            room.setRoomType(NonAcType);
            if(i % 5 == 0)
            {
                room.setRoomStatus("outoforder");
            }
            else
            {
                room.setRoomStatus("vacant");
                if(i % 4 == 0)
                {
                    room.setBedType("twin");
                    room.setViewType("parking");
                    room.setBathroomType("indian");
                }
                else if(i % 2 == 0)
                {
                    room.setBedType("queen");
                    room.setViewType("city");
                    room.setBathroomType("western");
                }
                else if(i % 3 == 0)
                {
                    room.setBedType("twin");
                    room.setViewType("parking");
                    room.setBathroomType("western");
                }
                else
                {
                    room.setBedType("queen");
                    room.setViewType("city");
                    room.setBathroomType("indian");
                }
            }
        }


        roomNo = 301;
        for(int i = 1; i <= 10; i++)
        {
            Room room = new Room();
            room.setRoomNumber(roomNo ++);
            room.setRoomType(deluxeType);
            if(i % 5 == 0)
            {
                room.setRoomStatus("outoforder");
            }
            else
            {
                room.setRoomStatus("vacant");
                if(i % 4 == 0)
                {
                    room.setBedType("twin");
                    room.setViewType("parking");
                    room.setBathroomType("indian");
                }
                else if(i % 2 == 0)
                {
                    room.setBedType("queen");
                    room.setViewType("city");
                    room.setBathroomType("western");
                }
                else if(i % 3 == 0)
                {
                    room.setBedType("twin");
                    room.setViewType("parking");
                    room.setBathroomType("western");
                }
                else
                {
                    room.setBedType("queen");
                    room.setViewType("city");
                    room.setBathroomType("indian");
                }
            }
        }
    }

/*    private void initialiseRoomStatus()
    {
        List<Room> roomsList = roomRepository.findByRoomStatus("vacant");
        for(Room r : roomsList)
        {
            RoomAvailabilityStatus roomAvailabilityStatus = new RoomAvailabilityStatus();
            roomAvailabilityStatus.setRoom(r);
            roomAvailabilityStatus.setStatus("vacant");
            roomAvailabilityStatus.setCheckinDate(null);
            roomAvailabilityStatus.setCheckoutDate(null);
            roomAvailabilityStatus.setCheckinTime(null);
            roomAvailabilityStatus.setCheckoutTime(null);
            statusRepository.save(roomAvailabilityStatus);
        }
*/
    // }

    private void initialiseAddOn()
    {
        StringBuffer name = new StringBuffer("mattress");
        Double cost = 150d;
        AddOns a = new AddOns(name.toString(), cost);
        addOnRepository.save(a);
        name = new StringBuffer("breakfast");
        cost = 200d;
        a = new AddOns(name.toString(), cost);
        addOnRepository.save(a);
        
    }
}
