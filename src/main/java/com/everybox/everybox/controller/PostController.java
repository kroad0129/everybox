package com.everybox.everybox.controller;

import com.everybox.everybox.domain.Post;
import com.everybox.everybox.service.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(
                request.getTitle(),
                request.getContent(),
                request.getLocation(),
                request.getUserId()
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
        private Long userId;
    }
}
