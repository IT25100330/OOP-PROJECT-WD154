package com.hotel.RuSiri.Controller.Reservation;

import com.hotel.RuSiri.DTO.Reservation.ReservationRequestDTO;
import com.hotel.RuSiri.DTO.Reservation.ReservationResponseDTO;
import com.hotel.RuSiri.DTO.Reservation.ReservationSummaryDTO;
import com.hotel.RuSiri.Service.Reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // * BOOK ROOM // Reserve Room *
    @PreAuthorize("hasRole('USER')")
    @PostMApping
    public ResponseEntity<ReservationResponseDTO> bookRoom(@RequestBody ReservationRequestDTO dto) {

        return ResponseEntity.ok(
                reservationService.bookRoom(
                        dto.getRoomId(),
                        dto.getCheckInDate(),
                        dto.getCheckOutDate()
                )
        );
    }

    // * MY RESERVATIONS *
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponseDTO>> getMyReservations() {
        return ResponseEntity.ok(reservationService.getMyReservations());
    }


    // * ADMIN VIEW *
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }


    // * CANCEL *
    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled");
    }

    // * GET RESERVATION BY ID *
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
    public ResponseEntity<ReservationSummaryDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
}