package com.hotel.RuSiri.DTO.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyProfileDTO {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber; // ✅
    private String nic;         // ✅
    private String firstName;
    private String lastName;
}
