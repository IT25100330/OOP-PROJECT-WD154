package com.hotel.RuSiri.DTO.Feedback;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Long roomId;
    private int rating;
    private String comment;
}
