package com.everybox.everybox.dto;

import com.everybox.everybox.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleUserDto {
    private Long id;
    private String nickname;

    public static SimpleUserDto from(User user) {
        return SimpleUserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
