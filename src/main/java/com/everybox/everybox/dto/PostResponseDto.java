package com.everybox.everybox.dto;

import com.everybox.everybox.domain.Post;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String details;
    private String location;
    private int quantity;
    private String imageUrl;
    private boolean isClosed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long giverId;
    private String giverNickname;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .details(post.getDetails())
                .location(post.getLocation())
                .quantity(post.getQuantity())
                .imageUrl(post.getImageUrl())
                .isClosed(post.isClosed())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .giverId(post.getGiver().getId())
                .giverNickname(post.getGiver().getNickname())
                .build();
    }
}
