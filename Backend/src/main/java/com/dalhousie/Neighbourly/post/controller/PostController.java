package com.dalhousie.Neighbourly.post.controller;

import com.dalhousie.Neighbourly.post.dto.PostRequest;
import com.dalhousie.Neighbourly.post.dto.PostResponse;
import com.dalhousie.Neighbourly.post.service.PostService;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;


    @PostMapping("/createPost")
    public ResponseEntity<CustomResponseBody<PostResponse>> createPost(@RequestBody PostRequest postRequest) {
        try {
            PostResponse response = postService.createPost(postRequest);
            return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Post created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
        }
    }

    @PutMapping("updatePost/{postId}")
    public ResponseEntity<CustomResponseBody<PostResponse>> updatePostContent(@PathVariable int postId, @RequestBody String content) {
        try{
            PostResponse response = postService.updatePostContent(postId, content);
        return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Post updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));   
        }
        
    }

    @PutMapping("/approve/{postId}")
    public ResponseEntity<CustomResponseBody<PostResponse>> approvePost(@PathVariable int postId) {
    try {
        PostResponse response = postService.approvePost(postId);
        return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Post approved successfully"));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
    }
}

    @DeleteMapping("/deletePost/{postId}")
    public ResponseEntity<CustomResponseBody<PostResponse>> deletePost(@PathVariable int postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, null, "Post deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
        }
    }


    @GetMapping("/approvedPosts/{neighbourhoodId}")
    public ResponseEntity<CustomResponseBody<List<PostResponse>>> getApprovedPosts(@PathVariable int neighbourhoodId) {
       try{
        List<PostResponse> response = postService.getApprovedPostsByNeighbourhood(neighbourhoodId);
        return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Approved posts retrieved successfully"));
       } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
       }
    }

      
  
  @GetMapping("/disapprovedPosts/{neighbourhoodId}")
  public ResponseEntity<CustomResponseBody<List<PostResponse>>> getDisapprovedPostsbyNeighbourhood(@PathVariable int neighbourhoodId) {
        try{
        List<PostResponse> response = postService.getDisapprovedPostsByNeighbourhood(neighbourhoodId);
        return ResponseEntity.ok(new CustomResponseBody<>(CustomResponseBody.Result.SUCCESS, response, "Disapproved posts retrieved successfully"));
        } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomResponseBody<>(CustomResponseBody.Result.FAILURE, null, e.getMessage()));
        }
  }

    
}

