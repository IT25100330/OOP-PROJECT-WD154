package com.hotel.RuSiri.DTO.User;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String currentPassword;
    private String newPassword;
}
