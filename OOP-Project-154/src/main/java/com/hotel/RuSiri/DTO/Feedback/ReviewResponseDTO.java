package com.hotel.RuSiri.DTO.Feedback;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponseDTO {
    private Long id;
    private String userName;
    private String roomNumber;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
