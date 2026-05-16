package com.hotel.RuSiri.Repository.Room;

import com.hotel.RuSiri.Entity.Room.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // 🔍 Find room by number
    Optional<Room> findByRoomNumber(String roomNumber);

    // 🔥 Get only available rooms
    List<Room> findByStatus(RoomStatus status);

    // 🔥 Filter by type
    List<Room> findByType(RoomType type);

    // 🔥 Combined filter (very useful later)
    List<Room> findByStatusAndType(RoomStatus status, RoomType type);

    List<Room> findByView(RoomView view);

    List<Room> findByAcType(AcType acType);

    List<Room> findByViewAndAcType(RoomView view, AcType acType);

    List<Room> findByStatusAndView(RoomStatus status, RoomView view);

    List<Room> findByStatusAndAcType(RoomStatus status, AcType acType);

    List<Room> findByStatusAndViewAndAcType(RoomStatus status, RoomView view, AcType acType);



}
