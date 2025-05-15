package com.everybox.everybox.service;

import com.everybox.everybox.domain.Post;
import com.everybox.everybox.domain.User;
import com.everybox.everybox.repository.PostRepository;
import com.everybox.everybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(String title, String details, String location, int quantity, String imageUrl, Long userId) {
        User giver = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = Post.builder()
                .title(title)
                .details(details)
                .location(location)
                .quantity(quantity)
                .imageUrl(imageUrl)
                .giver(giver)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isClosed(false)
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
