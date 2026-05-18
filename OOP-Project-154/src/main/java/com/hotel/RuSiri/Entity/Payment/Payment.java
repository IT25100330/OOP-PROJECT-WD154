package com.hotel.RuSiri.Entity.Payment;

import com.hotel.RuSiri.Entity.Reservation.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  One payment → one reservation
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    //  Amount
    private double amount;

    //  Method
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    //  Status
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    //  Time
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentType type;
}
