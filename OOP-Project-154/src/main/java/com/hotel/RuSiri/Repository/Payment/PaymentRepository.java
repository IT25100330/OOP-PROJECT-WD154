package com.hotel.RuSiri.Repository.Payment;

import com.hotel.RuSiri.Entity.Payment.Payment;
import com.hotel.RuSiri.Entity.Payment.PaymentType;
import com.hotel.RuSiri.Entity.Reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //  Get payment by reservation
    Optional<Payment> findByReservation(Reservation reservation);

    //  Get all payments for a reservation (future flexibility)
    List<Payment> findAllByReservation(Reservation reservation);

    Optional<Payment> findByReservationAndType(Reservation reservation, PaymentType type);

}
