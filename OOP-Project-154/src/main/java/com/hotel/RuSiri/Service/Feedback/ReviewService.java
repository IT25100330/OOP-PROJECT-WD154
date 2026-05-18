package com.hotel.RuSiri.Service.Feedback;

import com.hotel.RuSiri.DTO.Feedback.ReviewRequestDTO;
import com.hotel.RuSiri.DTO.Feedback.ReviewResponseDTO;
import com.hotel.RuSiri.Entity.Feedback.Review;
import com.hotel.RuSiri.Entity.Reservation.ReservationStatus;
import com.hotel.RuSiri.Entity.Room.Room;
import com.hotel.RuSiri.Entity.User.User;
import com.hotel.RuSiri.Repository.Feedback.ReviewRepository;
import com.hotel.RuSiri.Repository.Reservation.ReservationRepository;
import com.hotel.RuSiri.Repository.Room.RoomRepository;
import com.hotel.RuSiri.Repository.User.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         RoomRepository roomRepository,
                         UserRepository userRepository,
                         ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    //  ADD REVIEW
    public ReviewResponseDTO addReview(ReviewRequestDTO dto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        //  VALIDATION: only booked users can review
        boolean hasBooked = reservationRepository
                .existsByUserAndRoomAndStatus(user, room, ReservationStatus.CONFIRMED);

        //  VALIDATION (IMPORTANT)
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        if (!hasBooked) {
            throw new RuntimeException("You can only review rooms you have booked");
        }

        Review review = Review.builder()
                .user(user)
                .room(room)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        reviewRepository.save(review);

        return mapToDTO(review);
    }

    //  GET ROOM REVIEWS
    public List<ReviewResponseDTO> getRoomReviews(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return reviewRepository.findAllByRoom(room)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ReviewResponseDTO mapToDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userName(review.getUser().getUsername())
                .roomNumber(review.getRoom().getRoomNumber())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO dto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        //  Ownership check
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own review");
        }

        //  validation
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);

        return mapToDTO(review);
    }

    public void deleteReview(Long reviewId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        //  Ownership check
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own review");
        }

        reviewRepository.delete(review);
    }


}
