package com.everybox.everybox.repository;

import com.everybox.everybox.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByIsClosedFalse();
}
