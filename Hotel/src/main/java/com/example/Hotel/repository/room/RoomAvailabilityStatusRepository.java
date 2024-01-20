package com.example.Hotel.repository.room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.reservation.Booking;
import com.example.Hotel.entity.room.Room;
import com.example.Hotel.entity.room.RoomAvailabilityStatus;

@Repository
public interface RoomAvailabilityStatusRepository extends JpaRepository<RoomAvailabilityStatus, Long> {
        List<RoomAvailabilityStatus> findByStatus(String status);
        Optional<RoomAvailabilityStatus> findByBooking(Booking booking);

        // RoomAvailabilityStatusRepository.java

@Query("SELECT ras FROM RoomAvailabilityStatus ras " +
"LEFT JOIN FETCH ras.booking b " +
"LEFT JOIN FETCH b.customer c " +
"LEFT JOIN FETCH ras.room r " +
"LEFT JOIN FETCH r.roomType rt " +
"WHERE ras.checkinDate <= :date AND (ras.checkoutDate >= :date OR ras.checkoutDate IS NULL)")
List<RoomAvailabilityStatus> findWithDetailsByDate(@Param("date") LocalDate date);



        @Query("SELECT ras FROM RoomAvailabilityStatus ras WHERE " +
       "(ras.checkinDate <= :checkOutDate AND ras.checkoutDate >= :checkInDate) AND " +
       "(ras.checkinTime < :checkOutTime OR ras.checkoutTime > :checkInTime) AND " +
       "ras.status IN ('occupied')")
List<RoomAvailabilityStatus> findOccupiedOrReservedRoomsForDateTimeRange(
        @Param("checkInDate") LocalDate checkInDate, 
        @Param("checkInTime") LocalTime checkInTime, 
        @Param("checkOutDate") LocalDate checkOutDate, 
        @Param("checkOutTime") LocalTime checkOutTime);


        RoomAvailabilityStatus findByRoomAndCheckinDateAndCheckoutDate(Room room, LocalDate checkinDate, LocalDate checkoutDate);

        @Query("SELECT ras FROM RoomAvailabilityStatus ras WHERE ras.checkinDate <= :checkinDate AND ras.checkoutDate >= :checkoutDate")
        List<RoomAvailabilityStatus> findByCheckinDateAndCheckoutDateNotBetween(
        @Param("checkinDate") LocalDate checkinDate,
        @Param("checkoutDate") LocalDate checkoutDate
        );

        // Query to check overlapping bookings
        @Query("SELECT CASE WHEN COUNT(ras) > 0 THEN true ELSE false END FROM RoomAvailabilityStatus ras " +
        "WHERE ras.room.roomNumber = :roomNumber AND " +
        "((ras.checkinDate < :checkOut AND ras.checkoutDate > :checkIn) OR " +
        "(ras.checkinDate = :checkIn AND ras.checkoutDate = :checkOut))")
        boolean existsByRoomNumberAndOverlappingDates(@Param("roomNumber") Integer roomNumber,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut);

        List<RoomAvailabilityStatus> findByStatusAndCheckinDateAfter(String status, LocalDate checkinDate);
        List<RoomAvailabilityStatus> findByRoomAndCheckinDateAfter(Room room, LocalDate date);

        List<RoomAvailabilityStatus> findByCheckinDateAfterOrderByCheckinDateAsc(LocalDate startDate);

        List<RoomAvailabilityStatus> findByCheckinDateBeforeAndCheckoutDateAfter(LocalDate checkinDate, LocalDate checkoutDate);

        List<RoomAvailabilityStatus> findByStatusOrCheckinDateBeforeAndCheckoutDateAfter(String status, LocalDate startDate, LocalDate endDate);

        List<RoomAvailabilityStatus> findByCheckoutDateAndCheckoutTimeBetween(LocalDate checkoutDate, LocalTime startTime, LocalTime endTime);

        //for vacant
        List<RoomAvailabilityStatus> findByCheckinDateIsNullAndCheckoutDateIsNull();


        //for dirty
        List<RoomAvailabilityStatus> findByStatusOrCheckoutDate(String status, LocalDate checkout);

        List<RoomAvailabilityStatus> findByCheckinDateBetween(LocalDate checkinDate, LocalDate checkoutDate);

        List<RoomAvailabilityStatus> findByRoom(Room room);

        List<RoomAvailabilityStatus> findByRoom_RoomStatus(String status);
        
        List<RoomAvailabilityStatus> findByStatusAndCheckinDateBetween(String status, LocalDate startDate, LocalDate endDate);
        
        List<RoomAvailabilityStatus> findByCheckinDateAfter(LocalDate date);
        
        List<RoomAvailabilityStatus> findByStatusAndCheckoutDateLessThanEqualAndCheckoutTimeLessThanEqual(String status, LocalDate date, LocalTime time);
        
        List<RoomAvailabilityStatus> findByStatusAndCheckinDateBeforeAndCheckoutDateAfter(String status, LocalDate checkinDate, LocalDate checkoutDate);

        // List<RoomAvailabilityStatus> findByCheck
        
        List<RoomAvailabilityStatus> findByStatusAndCheckinDateIsNullAndCheckoutDateIsNull(String status);
        
        List<RoomAvailabilityStatus> findByStatusAndCheckinDate(String status, LocalDate date);
        
        List<RoomAvailabilityStatus> findByCheckoutDate(LocalDate date);

        // Query to find upcoming check-ins for a specific room including today
        //not required
        //List<RoomAvailabilityStatus> findByRoomAndCheckinDateGreaterThanEqual(Room room, LocalDate date);

        @Query("SELECT ras FROM RoomAvailabilityStatus ras WHERE ras.room = :room AND ras.checkinDate >= :currentDate ORDER BY ras.checkinDate ASC, ras.checkinTime ASC")
        List<RoomAvailabilityStatus> findAllUpcomingCheckInsForRoom(@Param("room") Room room, @Param("currentDate") LocalDate currentDate);

        @Query("SELECT ras FROM RoomAvailabilityStatus ras WHERE ras.checkinDate >= :currentDate ORDER BY ras.checkinDate ASC, ras.checkinTime ASC")
        List<RoomAvailabilityStatus> findAllUpcomingCheckIns( @Param("currentDate") LocalDate currentDate);

        @Query("SELECT ras FROM RoomAvailabilityStatus ras WHERE ras.room IN (" +
        "SELECT rasInner.room FROM RoomAvailabilityStatus rasInner WHERE " +
        "(rasInner.checkinDate < :checkOutDate AND rasInner.checkoutDate > :checkInDate) OR " +
        "(rasInner.checkinDate = :checkOutDate AND rasInner.checkinTime < :checkOutTime) OR " +
        "(rasInner.checkoutDate = :checkInDate AND rasInner.checkoutTime > :checkInTime)) " +
        "AND (ras.checkinDate IS NULL OR ras.checkoutDate IS NULL OR ras.checkinDate > :checkOutDate OR ras.checkoutDate < :checkInDate)")
        List<RoomAvailabilityStatus> findVacantRoomStatusesForDateTimeRange(
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkInTime") LocalTime checkInTime,
        @Param("checkOutDate") LocalDate checkOutDate,
        @Param("checkOutTime") LocalTime checkOutTime);

}
        // Custom query to find available rooms for given check-in and check-out dates and times
        /*  @Query("SELECT ras.room FROM RoomAvailabilityStatus ras WHERE " +
        "((ras.checkoutDate < :checkInDate) OR " +
        "(ras.checkoutDate = :checkInDate AND ras.checkoutTime < :checkInTime)) AND " +
        "((ras.checkinDate > :checkOutDate) OR " +
        "(ras.checkinDate = :checkOutDate AND ras.checkinTime > :checkOutTime)) AND " +
        "ras.status = 'vacant'")
        List<RoomAvailabilityStatus> findAvailableRoomsForDateTimeRange(
                @Param("checkInDate") LocalDate checkInDate,
                @Param("checkInTime") LocalTime checkInTime,
                @Param("checkOutDate") LocalDate checkOutDate,
                @Param("checkOutTime") LocalTime checkOutTime);


    // Find rooms that are due out for the next hour
        List<RoomAvailabilityStatus> findByStatusAndCheckoutDateAndCheckoutTimeBetween(
        String status,
        LocalDate currentDate,
        LocalTime checkOutTimeStart,
        LocalTime checkOutTimeEnd);

    
    /*  Find vacant rooms for a specific date and time range*/
        /*List<RoomAvailabilityStatus> findByStatusAndCheckinDateAfterAndCheckoutDateBefore(
        String status,
        LocalDate checkinDate,
        LocalDate checkoutDate);

    // Find rooms with a specific status and check-in date, excluding a certain status
        List<RoomAvailabilityStatus> findByStatusAndCheckinDateAndStatusNot(
        String status,
        LocalDate checkInDate,
        String notStatus);

        // Find rooms with a specific status and check-in date
        List<RoomAvailabilityStatus> findByStatusAndCheckinDate(
        String status,
        LocalDate checkInDate);

        // Find if a room is having a check in for given date
        Optional<RoomAvailabilityStatus> findByStatusAndCheckinDateAndRoom(
        String status,//dirty
        LocalDate checkInDate,
        Room room);

    // Find all currently vacant rooms
        List<RoomAvailabilityStatus> findByStatusAndCheckinDateIsNullAndCheckoutDateIsNull(String status);

        //find occupied rooms
        List<RoomAvailabilityStatus> findByStatusAndCheckinDateBeforeAndCheckoutDateAfter(String status, LocalDate checkinDate, LocalDate checkoutDate);

        //find by room
        Optional<RoomAvailabilityStatus> findByRoom(Room room);

    */
