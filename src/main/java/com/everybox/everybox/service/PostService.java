package com.everybox.everybox.service;

import com.everybox.everybox.domain.Post;
import com.everybox.everybox.domain.User;
import com.everybox.everybox.repository.PostRepository;
import com.everybox.everybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(String title, String content, String location, Long userId) {
        User giver = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = Post.builder()
                .title(title)
                .content(content)
                .location(location)
                .isClosed(false)
                .giver(giver)
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAvailablePosts() {
        return postRepository.findByIsClosedFalse();
    }
}
