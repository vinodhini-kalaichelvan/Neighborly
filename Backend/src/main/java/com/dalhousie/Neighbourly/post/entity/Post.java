package com.dalhousie.Neighbourly.post.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private int userId;
    private int neighbourhoodId;
    private String postType;
    private String postContent;
    private LocalDateTime dateTime;
    private boolean approved;
}
