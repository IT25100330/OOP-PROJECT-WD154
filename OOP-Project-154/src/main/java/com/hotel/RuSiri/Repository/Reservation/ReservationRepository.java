package com.hotel.RuSiri.Repository.Reservation;

import com.hotel.RuSiri.Entity.Reservation.Reservation;
import com.hotel.RuSiri.Entity.Reservation.ReservationStatus;
import com.hotel.RuSiri.Entity.Room.Room;
import com.hotel.RuSiri.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Get reservations of a user
    List<Reservation> findByUser(User user);

    // Get reservations of a room
    List<Reservation> findByRoom(Room room);

    // CHECK OVERLAPPING BOOKINGS (VERY IMPORTANT)
    List<Reservation> findByRoomAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Room room,
            ReservationStatus status,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );

    List<Reservation> findByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Room room,
            LocalDate checkOut,
            LocalDate checkIn
    );

    boolean existsByUserAndRoomAndStatus(
            User user,
            Room room,
            ReservationStatus status
    );

    @Query("""
        SELECT r.room.id FROM Reservation r
        WHERE r.status = 'CONFIRMED'
        AND r.checkInDate < :checkOut
        AND r.checkOutDate > :checkIn
        """)
    List<Long> findBookedRoomIds(LocalDate checkIn, LocalDate checkOut);
}
