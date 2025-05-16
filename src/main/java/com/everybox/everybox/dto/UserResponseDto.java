package com.everybox.everybox.dto;

import com.everybox.everybox.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Boolean isVerified;
    private String universityEmail; // 추가된 필드

    public static UserResponseDto from(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setIsVerified(user.getIsVerified());
        dto.setUniversityEmail(user.getUniversityEmail());
        return dto;
    }
}
