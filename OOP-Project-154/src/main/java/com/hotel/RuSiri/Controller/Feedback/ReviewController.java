package com.hotel.RuSiri.Controller.Feedback;

import com.hotel.RuSiri.DTO.Feedback.ReviewRequestDTO;
import com.hotel.RuSiri.DTO.Feedback.ReviewResponseDTO;
import com.hotel.RuSiri.Service.Feedback.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //  ADD REVIEW FOR ROOM if ONLY HE BOOKED THAT
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.addReview(dto));
    }

    //  GET REVIEWS BY ROOM
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviews(@PathVariable Long roomId) {
        return ResponseEntity.ok(reviewService.getRoomReviews(roomId));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequestDTO dto) {

        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully");
    }


}
