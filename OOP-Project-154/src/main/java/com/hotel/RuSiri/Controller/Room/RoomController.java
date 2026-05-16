package com.hotel.RuSiri.Controller.Room;

import com.hotel.RuSiri.DTO.Room.AvailableRoomDTO;
import com.hotel.RuSiri.DTO.Room.RoomRequestDTO;   // ✅ correct
import com.hotel.RuSiri.DTO.Room.RoomResponseDTO;
import com.hotel.RuSiri.Entity.Room.AcType;
import com.hotel.RuSiri.Entity.Room.RoomType;
import com.hotel.RuSiri.Entity.Room.RoomView;
import com.hotel.RuSiri.Service.Room.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // ================= CREATE ROOM (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO dto) {
        return ResponseEntity.ok(roomService.createRoom(dto));
    }

    // ================= UPDATE ROOM (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id,
                                                      @RequestBody RoomRequestDTO dto) {
        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }

    // ================= DEACTIVATE ROOM (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateRoom(@PathVariable Long id) {
        roomService.deactivateRoom(id);
        return ResponseEntity.ok("Room deactivated");
    }

    // ================= GET AVAILABLE ROOMS (USER + ADMIN) =================
    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAllAvailableRooms());
    }

    // ================= GET ALL ROOMS (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }


    // ================= GET ROOMS BY FILTER =================
    @GetMapping("/filter")
    public ResponseEntity<List<RoomResponseDTO>> getRooms(
            @RequestParam(required = false) RoomView view,
            @RequestParam(required = false) AcType acType,
            @RequestParam(required = false) RoomType type) {

        return ResponseEntity.ok(roomService.filterRooms(view, acType, type));
    }

    // ================= GET AVAILABLE ROOMS FOR DAY =================
    @GetMapping("/available")
    public ResponseEntity<List<AvailableRoomDTO>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        return ResponseEntity.ok(
                roomService.getAvailableRooms(checkIn, checkOut)
        );
    }

    @GetMapping("/{id}/check")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        return ResponseEntity.ok(
                roomService.isRoomAvailable(roomId, checkIn, checkOut)
        );
    }


}