package com.everybox.everybox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {
    private String nickname;
    private String password;
}
