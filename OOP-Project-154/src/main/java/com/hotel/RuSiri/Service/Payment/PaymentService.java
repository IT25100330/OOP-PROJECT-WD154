package com.hotel.RuSiri.Service.Payment;

import com.hotel.RuSiri.DTO.Payment.PaymentRequestDTO;
import com.hotel.RuSiri.DTO.Payment.PaymentResponseDTO;
import com.hotel.RuSiri.Entity.Notification.NotificationType;
import com.hotel.RuSiri.Entity.Payment.Payment;
import com.hotel.RuSiri.Entity.Payment.PaymentMethod;
import com.hotel.RuSiri.Entity.Payment.PaymentStatus;
import com.hotel.RuSiri.Entity.Payment.PaymentType;
import com.hotel.RuSiri.Entity.Reservation.Reservation;
import com.hotel.RuSiri.Entity.Reservation.ReservationStatus;
import com.hotel.RuSiri.Repository.Payment.PaymentRepository;
import com.hotel.RuSiri.Repository.Reservation.ReservationRepository;
import com.hotel.RuSiri.Service.Notification.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;


    private PaymentResponseDTO mapToDTO(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .reservationId(p.getReservation().getId())
                .roomNumber(p.getReservation().getRoom().getRoomNumber())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod().name())
                .status(p.getStatus().name())
                .paymentDate(p.getPaymentDate())
                .build();
    }


    public PaymentService(PaymentRepository paymentRepository,
                          ReservationRepository reservationRepository,
                          NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    //  PAY BOOKING FEE
    public PaymentResponseDTO makePayment(PaymentRequestDTO dto) {

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        double amount;

        //  Decide amount based on type
        if (dto.getType() == PaymentType.BOOKING_FEE) {

            //  prevent double booking fee
            paymentRepository.findByReservationAndType(reservation, PaymentType.BOOKING_FEE)
                    .ifPresent(p -> {
                        throw new RuntimeException("Booking fee already paid");
                    });

            amount = reservation.getRoom().getPrice() * 0.20;

            reservation.setBookingFee(amount);
            reservation.setStatus(ReservationStatus.CONFIRMED);

        } else if (dto.getType() == PaymentType.FINAL_PAYMENT) {

            double remaining = reservation.getTotalPrice() - reservation.getBookingFee();

            if (remaining <= 0) {
                throw new RuntimeException("Nothing left to pay");
            }

            amount = remaining;
        } else {
            throw new RuntimeException("Invalid payment type");
        }

        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(amount)
                .paymentMethod(dto.getMethod())
                .type(dto.getType()) //  important
                .status(PaymentStatus.SUCCESS)
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        reservationRepository.save(reservation);


        notificationService.createNotification(reservation.getUser(),
                "Payment successful for reservation #" + reservation.getId(),
                NotificationType.PAYMENT);

        return mapToDTO(payment);
    }


    //  GET PAYMENT BY RESERVATION 
    public List<PaymentResponseDTO> getPaymentByReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        List<Payment> payments = paymentRepository.findAllByReservation(reservation);

        if (payments.isEmpty()) {
            throw new RuntimeException("No payments found");
        }

        return payments.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // GET PAYMENT
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // DELETE PAYMENT
    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        paymentRepository.delete(payment);
    }


}
