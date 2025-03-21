package com.dalhousie.Neighbourly.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private int postId;
    private int userId;
    private int neighbourhoodId;
    private String postType;
    private String postContent;
    private String dateTime;
    private boolean approved;
}
