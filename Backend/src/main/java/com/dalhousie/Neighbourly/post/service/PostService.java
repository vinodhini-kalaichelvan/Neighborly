package com.dalhousie.Neighbourly.post.service;

import java.util.List;


import com.dalhousie.Neighbourly.post.dto.PostRequest;
import com.dalhousie.Neighbourly.post.dto.PostResponse;

public interface PostService {
    PostResponse createPost(PostRequest postRequest);
    PostResponse updatePostContent(int postId, String content);
    List<PostResponse> getApprovedPostsByNeighbourhood(int neighbourhoodId);
    PostResponse approvePost(int postId);
    void deletePost(int postId);
    List<PostResponse> getDisapprovedPostsByNeighbourhood(int neighbourhoodId);
}
