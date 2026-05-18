package com.hotel.RuSiri.Repository.Feedback;

import com.hotel.RuSiri.Entity.Feedback.Review;
import com.hotel.RuSiri.Entity.Reservation.ReservationStatus;
import com.hotel.RuSiri.Entity.Room.Room;
import com.hotel.RuSiri.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRoom(Room room);

}
