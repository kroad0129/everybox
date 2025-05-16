package com.everybox.everybox.service;

import com.everybox.everybox.domain.Post;
import com.everybox.everybox.domain.User;
import com.everybox.everybox.dto.UpdatePostRequestDto;
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

    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!post.getGiver().getId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }

    public Post updatePost(Long postId, Long userId, UpdatePostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!post.getGiver().getId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
        }
        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getDetails() != null) post.setDetails(request.getDetails());
        if (request.getLocation() != null) post.setLocation(request.getLocation());
        if (request.getQuantity() != null) post.setQuantity(request.getQuantity());
        if (request.getImageUrl() != null) post.setImageUrl(request.getImageUrl());
        if (request.getIsClosed() != null) post.setClosed(request.getIsClosed());
        post.setUpdatedAt(java.time.LocalDateTime.now());
        return postRepository.save(post);
    }
}
