package com.everybox.everybox.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int quantity;

    @Column(columnDefinition = "TEXT")
    private String details;

    private String location;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isClosed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User giver;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

