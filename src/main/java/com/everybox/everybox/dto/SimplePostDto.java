package com.everybox.everybox.dto;

import com.everybox.everybox.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimplePostDto {
    private Long id;
    private String title;

    public static SimplePostDto from(Post post) {
        return SimplePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .build();
    }
}
