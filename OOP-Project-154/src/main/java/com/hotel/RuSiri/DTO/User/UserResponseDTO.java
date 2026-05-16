package com.hotel.RuSiri.DTO.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    private String phoneNumber;
    private String nic;
}
