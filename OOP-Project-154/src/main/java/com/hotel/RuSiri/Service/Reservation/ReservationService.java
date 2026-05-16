package com.hotel.RuSiri.Service.Reservation;

import com.hotel.RuSiri.DTO.Reservation.ReservationResponseDTO;
import com.hotel.RuSiri.DTO.Reservation.ReservationSummaryDTO;
import com.hotel.RuSiri.DTO.Room.RoomResponseDTO;
import com.hotel.RuSiri.Entity.Notification.NotificationType;
import com.hotel.RuSiri.Entity.Reservation.Reservation;
import com.hotel.RuSiri.Entity.Reservation.ReservationStatus;
import com.hotel.RuSiri.Entity.Room.Room;
import com.hotel.RuSiri.Entity.Room.RoomStatus;
import com.hotel.RuSiri.Entity.User.User;
import com.hotel.RuSiri.Repository.Reservation.ReservationRepository;
import com.hotel.RuSiri.Repository.Room.RoomRepository;
import com.hotel.RuSiri.Repository.User.UserRepository;
import com.hotel.RuSiri.Service.Notification.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public ReservationService(ReservationRepository reservationRepository,
                              RoomRepository roomRepository,
                              UserRepository userRepository,
                              NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    private ReservationResponseDTO mapToDTO(Reservation r) {
        return ReservationResponseDTO.builder()
                .id(r.getId())
                .roomNumber(r.getRoom().getRoomNumber())
                .roomType(r.getRoom().getType().name())
                .checkInDate(r.getCheckInDate())
                .checkOutDate(r.getCheckOutDate())
                .totalPrice(r.getTotalPrice())
                .status(r.getStatus().name())
                .build();
    }

    // * BOOK ROOM *
    public ReservationResponseDTO bookRoom(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room is not available");
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new RuntimeException("Invalid date range");
        }

        List<Reservation> conflicts = reservationRepository
                .findByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                        room,
                        checkOut,
                        checkIn
                );

        conflicts = conflicts.stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING
                        || r.getStatus() == ReservationStatus.CONFIRMED)
                .toList();

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room already booked");
        }

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = days * room.getPrice();

        Reservation reservation = Reservation.builder()
                .user(user)
                .room(room)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .totalPrice(total)
                .status(ReservationStatus.PENDING)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        notificationService.createNotification(user,
                "Room " + room.getRoomNumber() + " booked successfully",
                NotificationType.BOOKING);

        return mapToDTO(saved);
    }
    // * MY RESERVATIONS *
    public List<ReservationResponseDTO> getMyReservations() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reservationRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // * CANCEL *
    public void cancelReservation(Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Ownership check
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can cancel only your reservation");
        }

        // Prevent double cancel
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        notificationService.createNotification(reservation.getUser(),
                "Reservation cancelled successfully",
                NotificationType.CANCELLATION);

    }

    // * ADMIN VIEW *
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }


    // * GET RESERVATION BY ID ADMIN // USER CAN SEE ONLY HIS RESERVATION *
    public ReservationSummaryDTO getReservationById(Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // USER can only see own reservation
        // ADMIN can see all
        if (role.equals("ROLE_USER") &&
                !reservation.getUser().getEmail().equals(email)) {

            throw new RuntimeException("Access denied");
        }

        return ReservationSummaryDTO.builder()
                .id(reservation.getId())
                .roomNumber(reservation.getRoom().getRoomNumber())
                .status(reservation.getStatus().name())
                .bookingFee(reservation.getBookingFee())
                .build();
    }


    private RoomResponseDTO mapRoomToDTO(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType())
                .price(room.getPrice())
                .build();
    }


}
