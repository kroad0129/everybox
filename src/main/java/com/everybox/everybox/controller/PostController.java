package com.everybox.everybox.controller;

import com.everybox.everybox.dto.CreatePostRequestDto;
import com.everybox.everybox.dto.PostResponseDto;
import com.everybox.everybox.dto.UpdatePostRequestDto;
import com.everybox.everybox.security.JwtAuthentication;
import com.everybox.everybox.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody CreatePostRequestDto request,
            Authentication authentication) {
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(PostResponseDto.from(postService.createPost(
                request.getTitle(),
                request.getDetails(),
                request.getLocation(),
                request.getQuantity(),
                request.getImageUrl(),
                userId
        )));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        return ResponseEntity.ok(
                postService.getAllPosts().stream()
                        .map(PostResponseDto::from)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(PostResponseDto.from(postService.getPostById(postId)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication) {
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDto request,
            Authentication authentication) {
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(
                PostResponseDto.from(postService.updatePost(postId, userId, request))
        );
    }
}
