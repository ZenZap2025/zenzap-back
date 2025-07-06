package com.zenzap.zenzap.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String username;
    private String email;
    private String birthday;
}

