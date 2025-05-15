package com.everybox.everybox.controller;

import com.everybox.everybox.domain.Post;
import com.everybox.everybox.service.PostService;
import com.everybox.everybox.security.JwtAuthentication;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest request, Authentication authentication) {
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();

        return postService.createPost(
                request.getTitle(),
                request.getContent(),
                request.getLocation(),
                userId
        );
    }

    @GetMapping
    public List<Post> getPosts() {
        return postService.getAvailablePosts();
    }

    @Data
    public static class CreatePostRequest {
        private String title;
        private String content;
        private String location;
        // ✅ userId 제거됨 (JWT에서 가져옴)
    }
}
