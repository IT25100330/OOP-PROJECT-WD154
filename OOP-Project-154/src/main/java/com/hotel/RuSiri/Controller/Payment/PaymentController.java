package com.hotel.RuSiri.Controller.Payment;


import com.hotel.RuSiri.DTO.Payment.PaymentRequestDTO;
import com.hotel.RuSiri.DTO.Payment.PaymentResponseDTO;
import com.hotel.RuSiri.Service.Payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //  CREATE PAYMENT
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponseDTO> pay(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.makePayment(dto));
    }

    //  GET PAYMENT BY RESERVATION
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/reservation/{id}")
    public ResponseEntity<List<PaymentResponseDTO>> getByReservation(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentByReservation(id));
    }

    //  ADMIN → GET ALL PAYMENTS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAll() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    //  DELETE PAYMENT (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePayment(
            @PathVariable Long id
    ) {

        paymentService.deletePayment(id);

        return ResponseEntity.ok("Payment deleted successfully");
    }

}
