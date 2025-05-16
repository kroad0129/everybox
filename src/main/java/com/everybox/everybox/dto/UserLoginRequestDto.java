package com.everybox.everybox.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String username;
    private String password;
}
