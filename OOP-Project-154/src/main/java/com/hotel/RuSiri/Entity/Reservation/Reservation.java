package com.hotel.RuSiri.Entity.Reservation;


import com.hotel.RuSiri.Entity.Room.Room;
import com.hotel.RuSiri.Entity.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // *MANY reservations → ONE user*
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // *MANY reservations → ONE room*
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // Dates
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // Price
    private double totalPrice;

    //Booking Fee
    private double bookingFee;

    // Status
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
