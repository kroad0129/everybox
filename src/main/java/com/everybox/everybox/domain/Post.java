package com.everybox.everybox.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private String content;
    private String location;
    private boolean isClosed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User giver;
}
