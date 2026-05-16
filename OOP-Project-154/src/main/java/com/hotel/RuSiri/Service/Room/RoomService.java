package com.hotel.RuSiri.Service.Room;

import com.hotel.RuSiri.DTO.Room.AvailableRoomDTO;
import com.hotel.RuSiri.DTO.Room.RoomRequestDTO;
import com.hotel.RuSiri.DTO.Room.RoomResponseDTO;
import com.hotel.RuSiri.Entity.Reservation.Reservation;
import com.hotel.RuSiri.Entity.Reservation.ReservationStatus;
import com.hotel.RuSiri.Entity.Room.*;
import com.hotel.RuSiri.Repository.Reservation.ReservationRepository;
import com.hotel.RuSiri.Repository.Room.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository,
                       ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    //MAP
    private RoomResponseDTO mapToDTO(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType())
                .view(room.getView())        // ✅
                .acType(room.getAcType())    // ✅
                .price(room.getPrice())
                .capacity(room.getCapacity())
                .description(room.getDescription())
                .status(room.getStatus())
                .build();
    }

    private AvailableRoomDTO mapToAvailableDTO(Room room) {
        return AvailableRoomDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType().name())
                .price(room.getPrice())
                .acType(room.getAcType())
                .view(room.getView())
                .build();
    }

    // ================= CREATE ROOM =================
    public RoomResponseDTO createRoom(RoomRequestDTO dto) {

        roomRepository.findByRoomNumber(dto.getRoomNumber())
                .ifPresent(r -> {
                    throw new RuntimeException("Room number already exists");
                });

        Room room = Room.builder()
                .roomNumber(dto.getRoomNumber())
                .type(dto.getType())
                .view(dto.getView())        // ✅
                .acType(dto.getAcType())    // ✅
                .price(dto.getPrice())
                .capacity(dto.getCapacity())
                .description(dto.getDescription())
                .status(RoomStatus.AVAILABLE)
                .build();

        Room saved = roomRepository.save(room);

        return mapToDTO(saved);
    }

    // ================= UPDATE ROOM =================
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO dto) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getType());
        room.setPrice(dto.getPrice());
        room.setCapacity(dto.getCapacity());
        room.setDescription(dto.getDescription());
        room.setStatus(dto.getStatus());
        room.setView(dto.getView());        // ✅
        room.setAcType(dto.getAcType());    // ✅

        Room updated = roomRepository.save(room);

        return mapToDTO(updated);
    }

    // ================= DEACTIVATE ROOM =================
    public void deactivateRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setStatus(RoomStatus.INACTIVE);

        roomRepository.save(room);
    }

    // ================= GET ALL AVAILABLE ROOMS =================
    public List<RoomResponseDTO> getAllAvailableRooms() {
        return roomRepository.findByStatus(RoomStatus.AVAILABLE)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ================= GET ALL ROOMS (ADMIN) =================
    public List<RoomResponseDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<RoomResponseDTO> filterRooms(RoomView view,
                                             AcType acType,
                                             RoomType type) {

        List<Room> rooms;

        if (view != null && acType != null) {
            rooms = roomRepository.findByStatusAndViewAndAcType(RoomStatus.AVAILABLE, view, acType);

        } else if (view != null) {
            rooms = roomRepository.findByStatusAndView(RoomStatus.AVAILABLE, view);

        } else if (acType != null) {
            rooms = roomRepository.findByStatusAndAcType(RoomStatus.AVAILABLE, acType);

        } else if (type != null) {
            rooms = roomRepository.findByStatusAndType(RoomStatus.AVAILABLE, type);

        } else {
            rooms = roomRepository.findByStatus(RoomStatus.AVAILABLE);
        }

        return rooms.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<AvailableRoomDTO> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {

        List<Room> rooms = roomRepository.findByStatus(RoomStatus.AVAILABLE);

        return rooms.stream()
                .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                .map(this::mapToAvailableDTO)
                .toList();
    }

    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {

        List<Reservation> conflicts =
                reservationRepository
                        .findByRoomAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                room,
                                ReservationStatus.CONFIRMED,
                                checkOut,
                                checkIn
                        );

        return conflicts.isEmpty();
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 🔥 Check overlap ONLY with CONFIRMED bookings
        List<Reservation> conflicts =
                reservationRepository
                        .findByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                room,
                                checkOut,
                                checkIn
                        );

        return conflicts.isEmpty(); // ✅ true = available
    }

}
